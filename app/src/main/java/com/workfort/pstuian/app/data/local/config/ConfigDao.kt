package com.workfort.pstuian.app.data.local.config

import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface ConfigDao {
    @Query("SELECT * FROM " + TableNames.CONFIG
            + " ORDER BY " + ColumnNames.Config.ID + " DESC LIMIT 1")
    suspend fun getLatest(): ConfigEntity?

    @Query("SELECT * FROM " + TableNames.CONFIG
            + " WHERE " + ColumnNames.Config.ID + "=:id")
    suspend fun get(id: Int): ConfigEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ConfigEntity)

    @Update
    suspend fun update(entity: ConfigEntity)

    @Delete
    suspend fun delete(entity: ConfigEntity)

    @Query("DELETE FROM " + TableNames.CONFIG)
    suspend fun deleteAll()
}