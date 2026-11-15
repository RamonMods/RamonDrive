package com.ramon.ramondrive.desktop.media

import com.ramon.ramondrive.core.media.MediaByteSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.sync.Mutex

internal class ActiveStream(
    val source: MediaByteSource,
    val mimeType: String
) {
    val readLock = Mutex()
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    var closed = false

    fun shutdown() {
        closed = true
        scope.cancel()
        runCatching { source.close() }
    }
}
