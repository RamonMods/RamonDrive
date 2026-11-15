package com.ramon.ramondrive.core.files

/**
 * Expands a rename pattern for every file in a batch.
 *
 * Placeholders: `{n}` becomes the position in the batch, `{name}` the original name without its
 * extension and `{ext}` the original extension without the dot. When the expansion has no
 * extension of its own the original one is kept, so `IMG_{n}` turns `photo.jpg` into `IMG_1.jpg`.
 */
object BulkRename {

    private val ILLEGAL = Regex("""[\\/:*?"<>|\u0000-\u001f]""")

    fun nameFor(pattern: String, originalName: String, index: Int): String {
        val originalDot = originalName.lastIndexOf('.')
        val hasExtension = originalDot > 0 && originalDot < originalName.lastIndex
        val base = if (hasExtension) originalName.substring(0, originalDot) else originalName
        val extension = if (hasExtension) originalName.substring(originalDot + 1) else ""

        val expanded = pattern
            .replace("{n}", index.toString())
            .replace("{name}", base)
            .replace("{ext}", extension)
            .trim()

        val cleaned = ILLEGAL.replace(expanded, "_").trim().trimEnd('.')
        if (cleaned.isEmpty()) return originalName

        val dot = cleaned.lastIndexOf('.')
        val hasOwnExtension = dot > 0 && dot < cleaned.lastIndex
        val result = if (hasOwnExtension || extension.isEmpty()) cleaned else "$cleaned.$extension"
        return result.take(MAX_NAME_LENGTH)
    }

    private const val MAX_NAME_LENGTH = 180
}
