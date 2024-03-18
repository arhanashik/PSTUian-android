package com.workfort.pstuian.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.model.BatchEntity

@Dao
interface BatchDao {
    @Query("SELECT * FROM " + TableNames.BATCH
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    suspend fun getAll(): List<BatchEntity>

    @Query("SELECT * FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.FACULTY_ID + "=:facultyId"
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    suspend fun getAll(facultyId: Int): List<BatchEntity>

    @Query("SELECT * FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.ID + "=:id")
    suspend fun get(id: Int): BatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(batchEntity: BatchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<BatchEntity>)

    @Update
    fun update(batchEntity: BatchEntity)

    @Delete
    fun delete(batchEntity: BatchEntity)

    @Query("DELETE FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.FACULTY_ID + "=:faculty")
    suspend fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.BATCH)
    suspend fun deleteAll()
}