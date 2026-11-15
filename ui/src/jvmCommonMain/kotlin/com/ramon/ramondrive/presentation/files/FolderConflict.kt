package com.ramon.ramondrive.presentation.files

import com.ramon.ramondrive.core.files.ImportSource

data class FolderConflict(
    val names: List<String>,
    val renamed: Map<String, String>,
    val sources: List<ImportSource>,
    val target: String?
)
