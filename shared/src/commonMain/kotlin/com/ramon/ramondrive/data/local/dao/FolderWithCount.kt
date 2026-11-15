package com.ramon.ramondrive.data.local.dao

import androidx.room.Embedded
import com.ramon.ramondrive.data.local.entity.FolderEntity

data class FolderWithCount(
    @Embedded val folder: FolderEntity,
    val fileCount: Int,
    val folderCount: Int
)
