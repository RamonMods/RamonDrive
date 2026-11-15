package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.data.remote.telegram.RemoteFolderState

internal sealed interface RemoteFolderRead {
    data class Ok(val state: RemoteFolderState) : RemoteFolderRead
    data object Unreadable : RemoteFolderRead
    data object Unavailable : RemoteFolderRead
}
