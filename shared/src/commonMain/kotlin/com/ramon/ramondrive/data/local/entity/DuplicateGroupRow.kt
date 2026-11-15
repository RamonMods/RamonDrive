package com.ramon.ramondrive.data.local.entity

/** One row of the duplicate scan: same folder, same name and same size. */
data class DuplicateGroupRow(
    val folderId: String?,
    val name: String,
    val sizeBytes: Long,
    val fileCount: Int
)
