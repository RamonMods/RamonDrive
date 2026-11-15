package com.ramon.ramondrive.core.files

data class LocalCleanup(
    val deletedCount: Int,
    val consentRequest: DeleteConsentRequest? = null,
    val keptOffline: Int = 0
)
