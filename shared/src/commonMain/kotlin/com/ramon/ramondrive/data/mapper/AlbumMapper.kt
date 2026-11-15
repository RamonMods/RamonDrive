package com.ramon.ramondrive.data.mapper

import com.ramon.ramondrive.data.local.entity.AlbumSummary
import com.ramon.ramondrive.domain.model.MediaAlbum

fun AlbumSummary.toDomain(): MediaAlbum = MediaAlbum(
    folderId = folderId,
    name = name,
    itemCount = itemCount,
    coverFileId = coverFileId,
    latestAt = latestAt
)
