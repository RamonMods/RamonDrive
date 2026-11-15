package com.ramon.ramondrive.data.local.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object RamonDriveDatabaseConstructor : RoomDatabaseConstructor<RamonDriveDatabase> {
    override fun initialize(): RamonDriveDatabase
}
