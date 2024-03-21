package com.workfort.pstuian.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.model.ConfigEntity

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