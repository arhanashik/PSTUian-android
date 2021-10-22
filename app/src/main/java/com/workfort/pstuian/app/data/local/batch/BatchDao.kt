package com.workfort.pstuian.app.data.local.batch

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface BatchDao {
    @Query("SELECT * FROM " + TableNames.BATCH
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    fun getAllLive(): LiveData<List<BatchEntity>>

    @Query("SELECT * FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.FACULTY_ID + "=:facultyId"
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    fun getAllLive(facultyId: Int): LiveData<List<BatchEntity>>

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