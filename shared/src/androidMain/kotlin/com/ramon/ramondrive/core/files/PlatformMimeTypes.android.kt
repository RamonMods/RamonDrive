package com.ramon.ramondrive.core.files

import android.webkit.MimeTypeMap

internal actual fun platformMimeTypeFromExtension(extension: String): String? =
    MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
