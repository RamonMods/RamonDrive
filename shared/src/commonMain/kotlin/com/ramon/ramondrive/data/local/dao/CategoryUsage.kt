package com.ramon.ramondrive.data.local.dao

import com.ramon.ramondrive.domain.model.FileCategory

data class CategoryUsage(
    val category: FileCategory,
    val fileCount: Int,
    val totalBytes: Long
)
