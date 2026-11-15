package com.ramon.ramondrive.presentation.preview

import com.ramon.ramondrive.domain.model.FileCategory
import com.ramon.ramondrive.domain.model.FileSortField
import com.ramon.ramondrive.domain.model.SortDirection
import com.ramon.ramondrive.presentation.navigation.Route

/** So paging sideways walks the same files in the same order, not a freshly guessed query. */
data class PreviewSequence(
    val folderId: String? = null,
    val filterByFolder: Boolean = false,
    val nameQuery: String? = null,
    val categories: List<FileCategory> = emptyList(),
    val favoritesOnly: Boolean = false,
    val availableOfflineOnly: Boolean = false,
    val hiddenOnly: Boolean = false,
    val archivedOnly: Boolean = false,
    val sortField: FileSortField = FileSortField.NAME,
    val sortDirection: SortDirection = SortDirection.ASCENDING
) {

    fun routeFor(fileId: String): Route.Preview = Route.Preview(
        fileId = fileId,
        folderId = folderId,
        filterByFolder = filterByFolder,
        nameQuery = nameQuery,
        categories = categories.takeIf { it.isNotEmpty() }
            ?.joinToString(CATEGORY_SEPARATOR) { it.name },
        favoritesOnly = favoritesOnly,
        availableOfflineOnly = availableOfflineOnly,
        hiddenOnly = hiddenOnly,
        archivedOnly = archivedOnly,
        sortField = sortField.name,
        sortDescending = sortDirection == SortDirection.DESCENDING
    )
}

const val CATEGORY_SEPARATOR = ","
