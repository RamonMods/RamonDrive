package com.ramon.ramondrive.data.mapper

import com.ramon.ramondrive.data.local.dao.FolderWithCount
import com.ramon.ramondrive.data.local.entity.FolderEntity
import com.ramon.ramondrive.domain.model.DriveFolder

fun FolderEntity.toDomain(): DriveFolder = DriveFolder(
    id = id,
    parentId = parentId,
    name = name,
    isHidden = isHidden,
    isArchived = isArchived,
    isFavorite = isFavorite,
    isAvailableOffline = isAvailableOffline,
    trashedAt = trashedAt,
    createdAt = createdAt,
    modifiedAt = modifiedAt
)

fun FolderWithCount.toDomain(): DriveFolder = folder.toDomain().copy(
    fileCount = fileCount,
    folderCount = folderCount
)
