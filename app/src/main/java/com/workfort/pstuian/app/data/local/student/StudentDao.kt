package com.workfort.pstuian.app.data.local.student

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface StudentDao {
    @Query("SELECT * FROM " + TableNames.STUDENT
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    fun getAllLive(): LiveData<List<StudentEntity>>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY_ID + "=:faculty"
            + " AND " + ColumnNames.Student.BATCH_ID + "=:batch"
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    fun getAllLive(faculty: String, batch: String): LiveData<List<StudentEntity>>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    suspend fun getAll(): List<StudentEntity>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY_ID + "=:facultyId"
            + " AND " + ColumnNames.Student.BATCH_ID + "=:batchId"
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    suspend fun getAll(facultyId: Int, batchId: Int): List<StudentEntity>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.ID + " = :id LIMIT 1")
    fun get(id: String): StudentEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentEntity: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<StudentEntity>)

    @Update
    suspend fun update(studentEntity: StudentEntity)

    @Delete
    suspend fun delete(studentEntity: StudentEntity)

    @Query("DELETE FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY_ID + "=:faculty")
    fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY_ID + "=:faculty"
            + " AND " + ColumnNames.Student.BATCH_ID + "=:batch")
    suspend fun deleteAll(faculty: String, batch: String)

    @Query("DELETE FROM " + TableNames.STUDENT)
    suspend fun deleteAll()
}