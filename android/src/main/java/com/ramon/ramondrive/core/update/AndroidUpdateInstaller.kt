package com.ramon.ramondrive.core.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.FileProvider
import com.ramon.ramondrive.core.common.SafeLog
import com.ramon.ramondrive.presentation.platform.InstallOutcome
import com.ramon.ramondrive.presentation.platform.UpdateInstaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/**
 * Downloads the release APK into the cache and hands it to the system installer, so the user only
 * taps "Atualizar agora" plus the Android confirmation dialog.
 */
class AndroidUpdateInstaller(private val context: Context) : UpdateInstaller {

    override val supported: Boolean = true

    override suspend fun downloadAndInstall(
        release: AppRelease,
        onProgress: (Float?) -> Unit
    ): InstallOutcome {
        val url = release.downloadUrl ?: return InstallOutcome.Failed("missing download url")
        if (!context.packageManager.canRequestPackageInstalls()) {
            openInstallPermission()
            return InstallOutcome.PermissionRequired
        }

        val file = try {
            withContext(Dispatchers.IO) {
                download(url, release.assetName ?: "RamonDrive-${release.version}.apk", onProgress)
            }
        } catch (t: Throwable) {
            SafeLog.w(TAG, "Update download failed", t)
            return InstallOutcome.Failed(t.message)
        }

        return withContext(Dispatchers.Main) { openInstaller(file) }
    }

    private fun download(url: String, name: String, onProgress: (Float?) -> Unit): File {
        val directory = File(context.cacheDir, UPDATES_DIR).apply { mkdirs() }
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
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, APK_MIME)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
        InstallOutcome.InstallerOpened
    } catch (t: Throwable) {
        SafeLog.w(TAG, "Installer intent failed", t)
        InstallOutcome.Failed(t.message)
    }

    private fun openInstallPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            .setData(Uri.parse("package:${context.packageName}"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(intent) }
    }

    private companion object {
        const val TAG = "AndroidUpdateInstaller"
        const val UPDATES_DIR = "updates"
        const val APK_MIME = "application/vnd.android.package-archive"
        const val USER_AGENT = "RamonDrive"
        const val TIMEOUT_MS = 30_000
        const val BUFFER_BYTES = 64 * 1024
    }
}
