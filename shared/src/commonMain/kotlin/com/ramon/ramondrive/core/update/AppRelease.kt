package com.ramon.ramondrive.core.update

data class AppRelease(
    val version: String,
    val notes: String,
    val pageUrl: String,
    /** Direct APK for this device, when the release publishes one. */
    val downloadUrl: String? = null,
    val assetName: String? = null
)
