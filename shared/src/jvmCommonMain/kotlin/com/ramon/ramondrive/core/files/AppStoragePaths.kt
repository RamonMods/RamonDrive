package com.ramon.ramondrive.core.files

import java.io.File

interface AppStoragePaths {

    val filesDir: File

    val cacheDir: File

    val externalCacheDir: File?

    val externalStorageRoot: File?
}
