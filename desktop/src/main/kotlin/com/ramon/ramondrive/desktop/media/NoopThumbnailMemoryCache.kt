package com.ramon.ramondrive.desktop.media

import com.ramon.ramondrive.core.media.ThumbnailMemoryCache

class NoopThumbnailMemoryCache : ThumbnailMemoryCache {

    override fun remove(fileId: String) {
    }

    override fun clear() {
    }
}
