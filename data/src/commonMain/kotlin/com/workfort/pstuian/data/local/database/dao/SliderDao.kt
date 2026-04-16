package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.SliderDbEntity

@Dao
interface SliderDao {
    @Query("SELECT * FROM slider ORDER BY id ASC")
    suspend fun getAll(): List<SliderDbEntity>

    @Query("SELECT * FROM slider WHERE id=:id LIMIT 1")
    suspend fun get(id: Int): SliderDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(slider: SliderDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sliders: List<SliderDbEntity>)

    @Update
    suspend fun update(slider: SliderDbEntity)

    @Delete
    suspend fun delete(slider: SliderDbEntity)

    @Query("DELETE FROM slider")
    suspend fun deleteAll()
}