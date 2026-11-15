package com.ramon.ramondrive.core.media

import java.io.File

interface MediaMetadataExtractor {

    fun extract(file: File, mimeType: String): MediaInfo
}
