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
            + " WHERE " + ColumnNames.Batch.FACULTY + "=:faculty"
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    fun getAllLive(faculty: String): LiveData<List<BatchEntity>>

    @Query("SELECT * FROM " + TableNames.BATCH
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    fun getAll(): List<BatchEntity>

    @Query("SELECT * FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.FACULTY + "=:faculty"
            + " ORDER BY " + ColumnNames.Batch.SESSION + " ASC")
    fun getAll(faculty: String): List<BatchEntity>

    @Query("SELECT * FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.SESSION + "=:session LIMIT 1")
    fun get(session: String): BatchEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(batchEntity: BatchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(batchEntities: List<BatchEntity>)

    @Update
    fun update(batchEntity: BatchEntity)

    @Delete
    fun delete(batchEntity: BatchEntity)

    @Query("DELETE FROM " + TableNames.BATCH
            + " WHERE " + ColumnNames.Batch.FACULTY + "=:faculty")
    fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.BATCH)
    fun deleteAll()
}