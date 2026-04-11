package com.workfort.pstuian.database

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE batch ADD COLUMN registered_student INTEGER NOT NULL DEFAULT 0")
    }
}
