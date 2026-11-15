package com.ramon.ramondrive.core.media

interface ThumbnailMemoryCache {

    fun remove(fileId: String)

    fun clear()
}
