package com.ramon.ramondrive.data.repository

import com.ramon.ramondrive.core.files.AppStoragePaths
import com.ramon.ramondrive.testing.unused
import java.io.File

internal class FakeStoragePaths : AppStoragePaths by unused() {
    private val root = File.createTempFile("ramondrive-paths", "").also {
        it.delete()
        it.mkdirs()
    }
    override val cacheDir: File = File(root, "cache").also { it.mkdirs() }
}
