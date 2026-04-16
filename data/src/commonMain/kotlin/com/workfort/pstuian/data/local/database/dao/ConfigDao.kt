package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.featuredomain.appconstant.ColumnNames
import com.workfort.pstuian.featuredomain.appconstant.TableNames
import com.workfort.pstuian.data.local.database.entity.ConfigDbEntity
import com.workfort.pstuian.featuredomain.model.ConfigEntity

@Dao
interface ConfigDao {
    @Query("SELECT * FROM config ORDER BY id DESC LIMIT 1")
    suspend fun getLatest(): ConfigDbEntity?

    @Query("SELECT * FROM config WHERE id=:id")
    suspend fun get(id: Int): ConfigDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ConfigDbEntity)

    @Update
    suspend fun update(entity: ConfigDbEntity)

    @Delete
    suspend fun delete(entity: ConfigDbEntity)

    @Query("DELETE FROM config")
    suspend fun deleteAll()
}
