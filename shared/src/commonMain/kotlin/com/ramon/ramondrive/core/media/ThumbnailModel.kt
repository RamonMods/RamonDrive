package com.ramon.ramondrive.core.media

data class ThumbnailModel(val fileId: String)

fun thumbnailCacheKey(fileId: String): String = "thumb:$fileId"
