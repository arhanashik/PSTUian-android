package com.workfort.pstuian.app.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 *  ****************************************************************************
 *  * Created by : arhan on 09 Dec, 2021 at 21:08.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE batch ADD COLUMN registered_student INTEGER NOT NULL DEFAULT 0")
    }
}