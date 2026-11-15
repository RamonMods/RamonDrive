package com.ramon.ramondrive.core.telegram

interface TdlibDatabaseKeyProvider {
    fun databaseKey(): ByteArray
}
