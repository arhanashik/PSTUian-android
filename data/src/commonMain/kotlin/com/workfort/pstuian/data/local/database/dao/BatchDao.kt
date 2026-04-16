package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.BatchDbEntity

@Dao
interface BatchDao {
    @Query("SELECT * FROM batch ORDER BY session ASC")
    suspend fun getAll(): List<BatchDbEntity>

    @Query("SELECT * FROM batch WHERE faculty_id=:facultyId ORDER BY session ASC")
    suspend fun getAll(facultyId: Int): List<BatchDbEntity>

    @Query("SELECT * FROM batch WHERE id=:id")
    suspend fun get(id: Int): BatchDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(batchEntity: BatchDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<BatchDbEntity>)

    @Update
    suspend fun update(batchEntity: BatchDbEntity)

    @Delete
    suspend fun delete(batchEntity: BatchDbEntity)

    @Query("DELETE FROM batch WHERE faculty_id=:faculty")
    suspend fun deleteAll(faculty: Int)

    @Query("DELETE FROM batch")
    suspend fun deleteAll()
}