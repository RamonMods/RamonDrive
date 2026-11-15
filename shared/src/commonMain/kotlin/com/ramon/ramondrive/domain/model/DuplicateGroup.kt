package com.ramon.ramondrive.domain.model

/** Files that share folder, name and size, so at most one of them is needed. */
data class DuplicateGroup(
    val folderId: String?,
    val name: String,
    val sizeBytes: Long,
    val files: List<DriveFile>
) {
    /** Everything but the most recently modified copy can go. */
    val removable: List<DriveFile>
        get() = files.sortedByDescending { it.modifiedAt }.drop(1)
}
