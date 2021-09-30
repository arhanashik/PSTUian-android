package com.workfort.pstuian.app.data.local.slider

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface SliderDao {
    @Query("SELECT * FROM " + TableNames.SLIDER
            + " ORDER BY " + ColumnNames.Slider.ID + " ASC")
    fun getAllLive(): LiveData<List<SliderEntity>>

    @Query("SELECT * FROM " + TableNames.SLIDER
            + " ORDER BY " + ColumnNames.Slider.ID + " ASC")
    fun getAll(): List<SliderEntity>

    @Query("SELECT * FROM " + TableNames.SLIDER
            + " WHERE " + ColumnNames.Slider.ID + "=:id LIMIT 1")
    fun get(id: String): SliderEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(slider: SliderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sliders: List<SliderEntity>)

    @Update
    fun update(slider: SliderEntity)

    @Delete
    fun delete(slider: SliderEntity)

    @Query("DELETE FROM " + TableNames.SLIDER)
    fun deleteAll()
}