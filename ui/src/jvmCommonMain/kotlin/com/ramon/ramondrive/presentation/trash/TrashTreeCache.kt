package com.ramon.ramondrive.presentation.trash

import com.ramon.ramondrive.domain.model.TrashItem

internal data class TrashTreeCache(
    val children: Map<String, List<TrashItem>> = emptyMap(),
    val counts: Map<String, Int> = emptyMap()
)
