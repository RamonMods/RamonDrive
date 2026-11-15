package com.ramon.ramondrive.core.files

import java.io.File
import java.nio.file.Files
import java.util.zip.Deflater
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Packs [directory] into a single ZIP inside [targetDir]. The folder name becomes the archive root,
 * so extracting it recreates the same tree. Empty folders are kept as directory entries.
 *
 * The archive is written to the import staging directory, which means the transfer cleanup deletes
 * it after the upload just like any other staged file.
 */
fun archiveDirectory(directory: String, targetDir: File): ImportedFile? {
    val root = runCatching { File(directory) }.getOrNull() ?: return null
    if (!root.isDirectory || !root.canRead() || root.list() == null) return null
    val base = root.parentFile ?: return null
    if (!targetDir.isDirectory && !targetDir.mkdirs()) return null

    val folderName = FileNameUtils.sanitize(root.name).ifEmpty { "folder" }
    val displayName = "$folderName.zip"
    // The staging name is random so two folders with the same name cannot truncate each other.
    val target = runCatching {
        File.createTempFile("ramondrive-", ".zip", targetDir)
    }.getOrNull() ?: return null

    return runCatching {
        ZipOutputStream(target.outputStream().buffered()).use { zip ->
            // Media is already compressed; speed matters more than squeezing it again.
            zip.setLevel(Deflater.BEST_SPEED)
            val targetPath = target.absoluteFile
            root.walkTopDown()
                .onEnter { dir ->
                    dir == root ||
                            runCatching { !Files.isSymbolicLink(dir.toPath()) }.getOrDefault(true)
                }
                .forEach { file ->
                    if (file.absoluteFile == targetPath) return@forEach
                    if (runCatching { Files.isSymbolicLink(file.toPath()) }.getOrDefault(false)) {
                        return@forEach
                    }
                    val entryName = file.relativeTo(base).invariantSeparatorsPath
                    when {
                        file.isDirectory -> {
                            // An unreadable directory must not be packed as if it were empty.
                            val children = file.list() ?: return@forEach
                            if (file != root && children.isNotEmpty()) return@forEach
                            zip.putNextEntry(ZipEntry("$entryName/").apply { time = file.lastModified() })
                            zip.closeEntry()
                        }

                        file.isFile -> {
                            zip.putNextEntry(ZipEntry(entryName).apply { time = file.lastModified() })
                            file.inputStream().buffered().use { input -> input.copyTo(zip) }
                            zip.closeEntry()
                        }
                    }
                }
        }
        ImportedFile(target.absolutePath, displayName, target.length())
    }.getOrElse {
        target.delete()
        null
    }
}
