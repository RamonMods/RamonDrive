package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.telegram.RemoteDocument
import com.ramon.ramondrive.data.local.entity.FileEntity
import com.ramon.ramondrive.data.remote.telegram.RemoteFileManifest

internal data class KnownRows(
    val manifests: Map<RemoteDocument, RemoteFileManifest?>,
    val byId: Map<String, FileEntity>,
    val byUniqueId: Map<String, FileEntity>,
    val splitCounts: Map<String, Int>,
    val firstPartStored: Set<String>
)
