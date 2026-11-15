package com.ramon.ramondrive.desktop.update

import com.ramon.ramondrive.core.common.SafeLog
import com.ramon.ramondrive.core.files.AppStoragePaths
import com.ramon.ramondrive.core.update.AppRelease
import com.ramon.ramondrive.presentation.platform.InstallOutcome
import com.ramon.ramondrive.presentation.platform.UpdateInstaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/**
 * Downloads the desktop installer for this OS and hands it to the system handler.
 *
 * A running app cannot silently replace itself, and installing over Program Files needs elevation,
 * so the best the desktop can do is open the installer — exactly what the Android flow does with
 * the package installer.
 */
class DesktopUpdateInstaller(private val paths: AppStoragePaths) : UpdateInstaller {

    override val supported: Boolean = true

    override suspend fun downloadAndInstall(
        release: AppRelease,
        onProgress: (Float?) -> Unit
    ): InstallOutcome {
        val url = release.downloadUrl ?: return InstallOutcome.Failed("missing download url")
        val file = try {
            withContext(Dispatchers.IO) {
                download(url, release.assetName ?: "RamonDrive-${release.version}-setup", onProgress)
            }
        } catch (t: Throwable) {
            SafeLog.w(TAG, "Update download failed", t)
            return InstallOutcome.Failed(t.message)
        }
        return withContext(Dispatchers.IO) { openInstaller(file) }
    }

    private fun download(url: String, name: String, onProgress: (Float?) -> Unit): File {
        val directory = File(paths.cacheDir, UPDATES_DIR).apply { mkdirs() }
        val target = File(directory, name)
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            instanceFollowRedirects = true
            connectTimeout = TIMEOUT_MS
            readTimeout = TIMEOUT_MS
            setRequestProperty("User-Agent", USER_AGENT)
        }
        try {
            if (connection.responseCode !in 200..299) error("HTTP ${connection.responseCode}")
            val total = connection.contentLengthLong
            var copied = 0L
            connection.inputStream.use { input ->
                target.outputStream().use { output ->
                    val buffer = ByteArray(BUFFER_BYTES)
                    while (true) {
                        val read = input.read(buffer)
                        if (read < 0) break
                        output.write(buffer, 0, read)
                        copied += read
                        onProgress(
                            if (total > 0) (copied.toFloat() / total).coerceIn(0f, 1f) else null
                        )
                    }
                }
            }
            if (total > 0 && target.length() != total) error("incomplete download")
        } catch (t: Throwable) {
            target.delete()
            throw t
        } finally {
            connection.disconnect()
        }
        return target
    }

    private fun openInstaller(file: File): InstallOutcome = try {
        val os = System.getProperty("os.name").orEmpty().lowercase()
        val command = when {
            os.contains("win") -> listOf("cmd", "/c", "start", "", file.absolutePath)
            os.contains("mac") -> listOf("open", file.absolutePath)
            else -> listOf("xdg-open", file.absolutePath)
        }
        ProcessBuilder(command).redirectErrorStream(true).start()
        InstallOutcome.InstallerOpened
    } catch (t: Throwable) {
        SafeLog.w(TAG, "Opening the installer failed", t)
        InstallOutcome.Failed(t.message)
    }

    private companion object {
        const val TAG = "DesktopUpdateInstaller"
        const val UPDATES_DIR = "updates"
        const val USER_AGENT = "RamonDrive"
        const val TIMEOUT_MS = 60_000
        const val BUFFER_BYTES = 64 * 1024
    }
}
