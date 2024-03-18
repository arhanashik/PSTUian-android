package com.workfort.pstuian.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.model.StudentEntity

@Dao
interface StudentDao {
    @Query("SELECT * FROM " + TableNames.STUDENT
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    suspend fun getAll(): List<StudentEntity>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY_ID + "=:facultyId"
            + " AND " + ColumnNames.Student.BATCH_ID + "=:batchId"
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    suspend fun getAll(facultyId: Int, batchId: Int): List<StudentEntity>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.ID + " = :id")
    suspend fun get(id: Int): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(studentEntity: StudentEntity)

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