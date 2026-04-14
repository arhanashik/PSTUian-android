package com.workfort.pstuian.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<AppDatabase> {
    val dbFile = context.getDatabasePath("pstuian.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
