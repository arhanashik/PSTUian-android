package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.FacultyDbEntity

@Dao
interface FacultyDao {
    @Query("SELECT * FROM faculty ORDER BY title ASC")
    suspend fun getAll(): List<FacultyDbEntity>

    @Query("SELECT * FROM faculty WHERE id = :id")
    suspend fun get(id: Int): FacultyDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(facultyEntity: FacultyDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(facultyEntities: List<FacultyDbEntity>)

    @Update
    suspend fun update(facultyEntity: FacultyDbEntity)

    @Delete
    suspend fun delete(facultyEntity: FacultyDbEntity)

    @Query("DELETE FROM faculty")
    suspend fun deleteAll()
}
