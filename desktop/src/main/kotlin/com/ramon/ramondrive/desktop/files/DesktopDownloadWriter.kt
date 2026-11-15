package com.ramon.ramondrive.desktop.files

import com.ramon.ramondrive.core.files.DownloadWriter
import com.ramon.ramondrive.core.files.FileNameUtils
import com.ramon.ramondrive.core.files.FilePartialDownload
import com.ramon.ramondrive.core.files.PartialDownload
import com.ramon.ramondrive.domain.repository.SettingsRepository
import java.io.File
import java.io.OutputStream
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class DesktopDownloadWriter(
    private val settingsRepository: SettingsRepository
) : DownloadWriter {

    override fun write(
        fileName: String,
        mimeType: String,
        folderPath: String?,
        body: (OutputStream) -> Unit
    ): String? = runCatching {
        val target = directoryFor(folderPath)
        target.mkdirs()
        val unique = FileNameUtils.uniqueName(FileNameUtils.sanitize(fileName)) { candidate ->
            File(target, candidate).exists()
        }
        val destination = File(target, unique)
        destination.outputStream().use(body)
        destination.absolutePath
    }.getOrNull()

    override fun openPartial(
        fileName: String,
        mimeType: String,
        folderPath: String?,
        token: String?
    ): PartialDownload? = runCatching {
        FilePartialDownload.open(directoryFor(folderPath), fileName, token)
    }.getOrNull()

    override fun freeBytes(folderPath: String?): Long? = runCatching {
        val target = directoryFor(folderPath)
        target.mkdirs()
        target.usableSpace.takeIf { it > 0 }
    }.getOrNull()

    private fun directoryFor(folderPath: String?): File {
        val configured = runBlocking { settingsRepository.preferences.first() }.downloadDirectory
        val base = configured?.let(::File)
            ?: File(System.getProperty("user.home"), "Downloads")
        val root = File(base, ROOT_DIR)
        return folderPath
            ?.split('/')
            ?.filter { it.isNotBlank() }
            ?.fold(root) { parent, segment -> File(parent, FileNameUtils.sanitize(segment)) }
            ?: root
    }

    private companion object {
        const val ROOT_DIR = "RamonDrive"
    }
}
