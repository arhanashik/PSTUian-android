package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.featuredomain.appconstant.ColumnNames
import com.workfort.pstuian.featuredomain.appconstant.TableNames
import com.workfort.pstuian.featuredomain.model.SliderEntity

@Dao
interface SliderDao {
    @Query("SELECT * FROM " + TableNames.SLIDER
            + " ORDER BY " + ColumnNames.Slider.ID + " ASC")
    suspend fun getAll(): List<SliderEntity>

    @Query("SELECT * FROM " + TableNames.SLIDER
            + " WHERE " + ColumnNames.Slider.ID + "=:id LIMIT 1")
    suspend fun get(id: Int): SliderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(slider: SliderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sliders: List<SliderEntity>)

    @Update
    suspend fun update(slider: SliderEntity)

    @Delete
    suspend fun delete(slider: SliderEntity)

    @Query("DELETE FROM " + TableNames.SLIDER)
    suspend fun deleteAll()
}