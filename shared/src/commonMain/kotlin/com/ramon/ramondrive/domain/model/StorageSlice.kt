package com.ramon.ramondrive.domain.model

data class StorageSlice(
    val category: FileCategory,
    val fileCount: Int,
    val totalBytes: Long
)
