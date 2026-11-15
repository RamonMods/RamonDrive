package com.ramon.ramondrive.presentation.search

import com.ramon.ramondrive.domain.model.FileCategory
import com.ramon.ramondrive.domain.model.FileSortField
import com.ramon.ramondrive.domain.model.SortDirection

data class SearchFilters(
    val category: FileCategory? = null,
    val backedUpOnly: Boolean = false,
    val notBackedUpOnly: Boolean = false,
    val minSizeMb: Int? = null,
    val sortField: FileSortField = FileSortField.DATE_MODIFIED,
    val sortDirection: SortDirection = SortDirection.DESCENDING
)
