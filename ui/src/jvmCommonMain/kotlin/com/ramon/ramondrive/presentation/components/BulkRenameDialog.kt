package com.ramon.ramondrive.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.ramon.ramondrive.core.files.BulkRename
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.common_cancel
import com.ramon.ramondrive.resources.common_rename
import com.ramon.ramondrive.resources.files_bulk_rename
import com.ramon.ramondrive.resources.files_bulk_rename_hint
import com.ramon.ramondrive.resources.files_bulk_rename_pattern
import com.ramon.ramondrive.resources.files_bulk_rename_preview
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BulkRenameDialog(
    names: List<String>,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var pattern by rememberSaveable { mutableStateOf("{name}_{n}") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.files_bulk_rename)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = pattern,
                    onValueChange = { pattern = it },
                    singleLine = true,
                    label = { Text(stringResource(Res.string.files_bulk_rename_pattern)) }
                )
                Text(
                    text = stringResource(Res.string.files_bulk_rename_hint),
                    style = MaterialTheme.typography.bodySmall
                )
                if (pattern.isNotBlank()) {
                    Text(
                        text = stringResource(Res.string.files_bulk_rename_preview),
                        style = MaterialTheme.typography.labelLarge
                    )
                    names.take(PREVIEW_SIZE).forEachIndexed { index, name ->
                        Text(
                            text = "$name → ${BulkRename.nameFor(pattern, name, index + 1)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(pattern.trim()) },
                shapes = ButtonDefaults.shapes(),
                enabled = pattern.isNotBlank()
            ) { Text(stringResource(Res.string.common_rename)) }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shapes = ButtonDefaults.shapes()
            ) { Text(stringResource(Res.string.common_cancel)) }
        }
    )
}

private const val PREVIEW_SIZE = 4
