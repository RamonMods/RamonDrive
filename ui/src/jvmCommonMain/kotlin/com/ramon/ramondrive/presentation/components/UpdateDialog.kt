package com.ramon.ramondrive.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramon.ramondrive.core.update.AppRelease
import com.ramon.ramondrive.presentation.common.MarkdownText
import com.ramon.ramondrive.presentation.update.UpdateInstallState
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.about_update_download
import com.ramon.ramondrive.resources.about_update_from
import com.ramon.ramondrive.resources.about_update_later
import com.ramon.ramondrive.resources.about_update_title
import com.ramon.ramondrive.resources.update_downloading
import com.ramon.ramondrive.resources.update_downloading_percent
import com.ramon.ramondrive.resources.update_failed
import com.ramon.ramondrive.resources.update_install_now
import com.ramon.ramondrive.resources.update_open_page
import com.ramon.ramondrive.resources.update_permission_needed
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Composable
fun UpdateDialog(
    release: AppRelease,
    currentVersion: String,
    canInstall: Boolean,
    installState: UpdateInstallState,
    onOpenUrl: (String) -> Unit,
    onInstall: () -> Unit,
    onOpenPage: () -> Unit,
    onDismiss: () -> Unit
) {
    val downloading = installState as? UpdateInstallState.Downloading
    val inAppInstall = canInstall && release.downloadUrl != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.about_update_title, release.version)) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = stringResource(
                        Res.string.about_update_from,
                        currentVersion,
                        release.version
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                when (installState) {
                    is UpdateInstallState.Downloading -> {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = installState.progress?.let {
                                stringResource(
                                    Res.string.update_downloading_percent,
                                    (it * 100).roundToInt()
                                )
                            } ?: stringResource(Res.string.update_downloading),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { installState.progress ?: 0f },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    UpdateInstallState.PermissionRequired -> {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(Res.string.update_permission_needed),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    is UpdateInstallState.Failed -> {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(Res.string.update_failed),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    else -> Unit
                }

                if (release.notes.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    MarkdownText(
                        text = release.notes,
                        onOpenUrl = onOpenUrl
                    )
                }

                if (inAppInstall) {
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onOpenPage) {
                        Text(stringResource(Res.string.update_open_page))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = if (inAppInstall) onInstall else onOpenPage,
                enabled = downloading == null,
                shapes = ButtonDefaults.shapes()
            ) {
                Text(
                    when {
                        downloading != null -> stringResource(Res.string.update_downloading)
                        inAppInstall -> stringResource(Res.string.update_install_now)
                        else -> stringResource(Res.string.about_update_download)
                    }
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = downloading == null,
                shapes = ButtonDefaults.shapes()
            ) {
                Text(stringResource(Res.string.about_update_later))
            }
        }
    )
}
