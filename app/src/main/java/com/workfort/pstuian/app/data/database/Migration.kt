package com.workfort.pstuian.app.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE batch ADD COLUMN registered_student INTEGER NOT NULL DEFAULT 0")
    }
}