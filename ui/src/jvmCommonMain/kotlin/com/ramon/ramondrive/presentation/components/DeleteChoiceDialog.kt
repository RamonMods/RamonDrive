package com.ramon.ramondrive.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.common_cancel
import com.ramon.ramondrive.resources.common_delete_forever
import com.ramon.ramondrive.resources.common_move_trash
import org.jetbrains.compose.resources.stringResource

/**
 * Offers the two delete paths instead of silently sending everything to the trash: the reversible
 * one, and the one that also removes the messages from Telegram and the downloaded copy.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DeleteChoiceDialog(
    title: String,
    message: String,
    onMoveToTrash: () -> Unit,
    onDeleteForever: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = title, style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(12.dp))
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onMoveToTrash,
                    shapes = ButtonDefaults.shapes(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.common_move_trash))
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onDeleteForever,
                    shapes = ButtonDefaults.shapes(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.common_delete_forever))
                }
                Spacer(Modifier.height(4.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(Res.string.common_cancel))
                }
            }
        }
    }
}
