package com.ramon.ramondrive.presentation.components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.common_empty
import com.ramon.ramondrive.resources.file_count
import com.ramon.ramondrive.resources.folder_count
import com.ramon.ramondrive.domain.model.DriveFolder

@Composable
fun DriveFolder.contentsLabel(): String {
    val parts = buildList {
        if (folderCount > 0) {
            add(pluralStringResource(Res.plurals.folder_count, folderCount, folderCount))
        }
        if (fileCount > 0) {
            add(pluralStringResource(Res.plurals.file_count, fileCount, fileCount))
        }
    }
    return if (parts.isEmpty()) stringResource(Res.string.common_empty) else parts.joinToString(" · ")
}
