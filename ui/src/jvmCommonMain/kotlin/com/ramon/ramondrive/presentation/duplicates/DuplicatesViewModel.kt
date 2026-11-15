package com.ramon.ramondrive.presentation.duplicates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramon.ramondrive.core.common.AppResult
import com.ramon.ramondrive.domain.repository.FileRepository
import com.ramon.ramondrive.domain.repository.TrashRepository
import com.ramon.ramondrive.presentation.common.UiText
import com.ramon.ramondrive.presentation.common.toUiText
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.duplicates_done
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DuplicateGroupUi(
    val folderId: String?,
    val folderName: String,
    val name: String,
    val sizeBytes: Long,
    val count: Int,
    val removableIds: List<String>
)

data class DuplicatesUiState(
    val loading: Boolean = true,
    val groups: List<DuplicateGroupUi> = emptyList(),
    val working: Boolean = false
) {
    val removableCount: Int get() = groups.sumOf { it.removableIds.size }
    val reclaimableBytes: Long get() = groups.sumOf { it.sizeBytes * it.removableIds.size }
}

class DuplicatesViewModel(
    private val fileRepository: FileRepository,
    private val trashRepository: TrashRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DuplicatesUiState())
    val uiState: StateFlow<DuplicatesUiState> = _uiState.asStateFlow()

    private val _messages = MutableSharedFlow<UiText>(extraBufferCapacity = 8)
    val messages = _messages.asSharedFlow()

    private val folderNames = mutableMapOf<String, String>()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val groups = fileRepository.duplicateGroups().map { group ->
                val folderName = group.folderId?.let { id ->
                    folderNames.getOrPut(id) { fileRepository.getFolder(id)?.name.orEmpty() }
                }.orEmpty()
                DuplicateGroupUi(
                    folderId = group.folderId,
                    folderName = folderName,
                    name = group.name,
                    sizeBytes = group.sizeBytes,
                    count = group.files.size,
                    removableIds = group.removable.map { it.id }
                )
            }
            _uiState.update { it.copy(loading = false, groups = groups) }
        }
    }

    fun clean() {
        val ids = _uiState.value.groups.flatMap { it.removableIds }
        if (ids.isEmpty()) return
        viewModelScope.launch {
            _uiState.update { it.copy(working = true) }
            val result = withContext(NonCancellable) { trashRepository.moveFilesToTrash(ids) }
            _uiState.update { it.copy(working = false) }
            when (result) {
                is AppResult.Success -> {
                    _messages.tryEmit(UiText.Resource(Res.string.duplicates_done, ids.size))
                    refresh()
                }

                is AppResult.Failure -> _messages.tryEmit(result.error.toUiText())
            }
        }
    }
}
