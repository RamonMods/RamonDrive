package com.ramon.ramondrive.presentation.duplicates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramon.ramondrive.presentation.common.CollectSnackbarMessages
import com.ramon.ramondrive.presentation.common.Formatters
import com.ramon.ramondrive.presentation.components.BlockingProgressDialog
import com.ramon.ramondrive.presentation.components.ConfirmDialog
import com.ramon.ramondrive.presentation.components.EmptyState
import com.ramon.ramondrive.presentation.components.LoadingState
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.common_back
import com.ramon.ramondrive.resources.common_move_trash
import com.ramon.ramondrive.resources.common_refresh
import com.ramon.ramondrive.resources.duplicates_clean
import com.ramon.ramondrive.resources.duplicates_confirm_message
import com.ramon.ramondrive.resources.duplicates_confirm_title
import com.ramon.ramondrive.resources.duplicates_empty_message
import com.ramon.ramondrive.resources.duplicates_empty_title
import com.ramon.ramondrive.resources.duplicates_group_details
import com.ramon.ramondrive.resources.duplicates_root
import com.ramon.ramondrive.resources.duplicates_subtitle
import com.ramon.ramondrive.resources.duplicates_summary
import com.ramon.ramondrive.resources.duplicates_title
import com.ramon.ramondrive.resources.duplicates_working
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuplicatesScreen(
    onBack: () -> Unit,
    viewModel: DuplicatesViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var confirmClean by remember { mutableStateOf(false) }

    CollectSnackbarMessages(viewModel.messages, snackbarHostState)

    if (state.working) {
        BlockingProgressDialog(message = stringResource(Res.string.duplicates_working))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.duplicates_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.common_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh, enabled = !state.loading) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = stringResource(Res.string.common_refresh)
                        )
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.loading -> LoadingState(modifier = Modifier.padding(padding))

            state.groups.isEmpty() -> EmptyState(
                icon = Icons.Filled.CheckCircle,
                title = stringResource(Res.string.duplicates_empty_title),
                description = stringResource(Res.string.duplicates_empty_message),
                modifier = Modifier.padding(padding)
            )

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    SummaryCard(
                        state = state,
                        onClean = { confirmClean = true }
                    )
                }
                items(
                    items = state.groups,
                    key = { group -> "${group.folderId}:${group.name}:${group.sizeBytes}" }
                ) { group ->
                    Card {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.titleSmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = stringResource(
                                    Res.string.duplicates_group_details,
                                    group.folderName.ifBlank {
                                        stringResource(Res.string.duplicates_root)
                                    },
                                    group.count,
                                    Formatters.bytes(group.sizeBytes)
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (confirmClean) {
        ConfirmDialog(
            title = stringResource(Res.string.duplicates_confirm_title),
            message = stringResource(
                Res.string.duplicates_confirm_message,
                state.removableCount
            ),
            confirmLabel = stringResource(Res.string.common_move_trash),
            destructive = true,
            onConfirm = {
                confirmClean = false
                viewModel.clean()
            },
            onDismiss = { confirmClean = false }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SummaryCard(
    state: DuplicatesUiState,
    onClean: () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(
                    Res.string.duplicates_summary,
                    state.groups.size,
                    Formatters.bytes(state.reclaimableBytes)
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.duplicates_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onClean,
                enabled = state.removableCount > 0,
                shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(stringResource(Res.string.duplicates_clean, state.removableCount))
            }
        }
    }
}
