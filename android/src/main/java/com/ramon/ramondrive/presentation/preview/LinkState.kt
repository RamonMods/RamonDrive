package com.ramon.ramondrive.presentation.preview

import com.ramon.ramondrive.domain.model.LinkMetadata

internal sealed interface LinkState {
    data object Loading : LinkState
    data object Bare : LinkState
    data class Article(val metadata: LinkMetadata) : LinkState
}
