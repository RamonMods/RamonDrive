package com.ramon.ramondrive.presentation.platform

import androidx.compose.runtime.staticCompositionLocalOf
import com.ramon.ramondrive.core.files.DeleteConsentRequest

fun interface DeleteConsentLauncher {

    fun launch(request: DeleteConsentRequest, onResult: (Boolean) -> Unit)
}

val LocalDeleteConsentLauncher = staticCompositionLocalOf<DeleteConsentLauncher> {
    error("DeleteConsentLauncher is not provided")
}
