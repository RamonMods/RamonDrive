package com.ramon.ramondrive.core.common

fun interface LogSink {
    fun write(level: Char, tag: String, message: String, throwable: Throwable?)
}
