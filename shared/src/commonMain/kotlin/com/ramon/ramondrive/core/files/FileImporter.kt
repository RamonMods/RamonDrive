package com.ramon.ramondrive.core.files

/** References are platform URIs on Android and plain file paths on desktop. */
interface FileImporter {

    fun import(reference: String): ImportedFile?

    /** A folder yields one entry per file inside it; anything else yields the reference itself. */
    fun expand(reference: String): List<ImportSource>

    /** Packs a folder into a single archive in the staging area; null when it cannot be read. */
    fun archiveFolder(reference: String): ImportedFile?

    fun discard(imported: ImportedFile)

    fun isStaged(path: String): Boolean

    fun sweepOrphans(referencedPaths: Set<String>)
}
