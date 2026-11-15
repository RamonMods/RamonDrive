package com.ramon.ramondrive.core.update

import com.ramon.ramondrive.core.common.SafeLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

/**
 * An anonymous read of a public endpoint, so a check reveals no more than visiting the releases
 * page.
 */
class UpdateChecker(
    private val currentVersion: String,
    /** Device ABI, so the release can point straight at the matching APK. */
    private val abi: String? = null
) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun newerRelease(): AppRelease? = withContext(Dispatchers.IO) {
        val payload = runCatching { fetchLatest() }
            .onFailure { SafeLog.w(TAG, "Update check failed", it) }
            .getOrNull()
            ?: return@withContext null

        val release = runCatching {
            json.decodeFromString(GitHubRelease.serializer(), payload)
        }.getOrNull() ?: return@withContext null

        if (release.draft || release.preRelease) return@withContext null
        val latest = release.tag.removePrefix("v").trim()
        if (!isNewer(latest, currentVersion)) return@withContext null

        val asset = release.apkAsset(abi)
        AppRelease(
            version = latest,
            notes = release.body.orEmpty().trim(),
            pageUrl = release.pageUrl,
            downloadUrl = asset?.downloadUrl,
            assetName = asset?.name
        )
    }

    private fun GitHubRelease.apkAsset(abi: String?): GitHubAsset? {
        val apks = assets.filter { it.name.endsWith(".apk", ignoreCase = true) }
        if (apks.isEmpty()) return null

        val wanted = abi?.lowercase()?.trim()?.takeIf { it.isNotEmpty() }
        if (wanted != null) {
            apks.firstOrNull { it.name.lowercase().contains("-$wanted-") }?.let { return it }
            apks.firstOrNull { it.name.lowercase().contains(wanted) }?.let { return it }
            // A 64-bit phone still runs the 32-bit build, which beats the universal one.
            if (wanted == "arm64-v8a") {
                apks.firstOrNull { it.name.lowercase().contains("armeabi-v7a") }?.let { return it }
            }
        }
        return apks.firstOrNull { it.name.contains("universal", ignoreCase = true) } ?: apks.first()
    }

    private fun fetchLatest(): String {
        val connection = (URL(RELEASES_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = TIMEOUT_MS
            readTimeout = TIMEOUT_MS
            setRequestProperty("Accept", "application/vnd.github+json")
            setRequestProperty("User-Agent", USER_AGENT)
        }
        return try {
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                error("HTTP ${connection.responseCode}")
            }
            connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    /** Compares dotted numbers so 1.10 counts as newer than 1.9. */
    private fun isNewer(candidate: String, current: String): Boolean {
        val left = candidate.numbers()
        val right = current.removePrefix("v").numbers()
        if (left.isEmpty()) return false
        for (index in 0 until maxOf(left.size, right.size)) {
            val a = left.getOrElse(index) { 0 }
            val b = right.getOrElse(index) { 0 }
            if (a != b) return a > b
        }
        return false
    }

    private fun String.numbers(): List<Int> = split('.', '-', '_')
        .mapNotNull { part -> part.takeWhile(Char::isDigit).toIntOrNull() }

    private companion object {
        const val TAG = "UpdateChecker"
        const val RELEASES_URL =
            "https://api.github.com/repos/RamonMods/RamonDrive/releases/latest"
        const val USER_AGENT = "RamonDrive"
        const val TIMEOUT_MS = 15_000
    }
}
