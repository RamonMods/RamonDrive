package com.ramon.ramondrive.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.stringResource
import com.ramon.ramondrive.resources.Res
import com.ramon.ramondrive.resources.common_category_archives
import com.ramon.ramondrive.resources.common_category_audio
import com.ramon.ramondrive.resources.common_category_documents
import com.ramon.ramondrive.resources.common_category_other
import com.ramon.ramondrive.resources.common_category_photos
import com.ramon.ramondrive.resources.common_category_videos
import com.ramon.ramondrive.domain.model.FileCategory
import com.ramon.ramondrive.presentation.theme.ChartPalette
import com.ramon.ramondrive.presentation.theme.harmonizedWithPrimary

@Composable
fun FileCategory.label(): String = stringResource(
    when (this) {
        FileCategory.IMAGE -> Res.string.common_category_photos
        FileCategory.VIDEO -> Res.string.common_category_videos
        FileCategory.AUDIO -> Res.string.common_category_audio
        FileCategory.DOCUMENT -> Res.string.common_category_documents
        FileCategory.ARCHIVE -> Res.string.common_category_archives
        FileCategory.OTHER -> Res.string.common_category_other
    }
)

@Composable
fun FileCategory.chartColor(): Color = when (this) {
    FileCategory.IMAGE -> ChartPalette.Blue
    FileCategory.VIDEO -> ChartPalette.Violet
    FileCategory.AUDIO -> ChartPalette.Teal
    FileCategory.DOCUMENT -> ChartPalette.Amber
    FileCategory.ARCHIVE -> ChartPalette.Rose
    FileCategory.OTHER -> ChartPalette.Slate
}.harmonizedWithPrimary()
