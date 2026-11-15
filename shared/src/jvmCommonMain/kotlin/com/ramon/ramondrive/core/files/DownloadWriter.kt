package com.ramon.ramondrive.core.files

import java.io.OutputStream

interface DownloadWriter {

    fun write(
        fileName: String,
        mimeType: String,
        folderPath: String? = null,
        body: (OutputStream) -> Unit
    ): String?

    fun openPartial(
        fileName: String,
        mimeType: String,
        folderPath: String?,
        token: String?
    ): PartialDownload?

    fun freeBytes(folderPath: String?): Long?

    /**
     * True only for a copy this app wrote into its own public download folder. Permanent deletion
     * uses it to avoid touching the user's original files that were merely backed up.
     */
    fun owns(path: String): Boolean = false
}
