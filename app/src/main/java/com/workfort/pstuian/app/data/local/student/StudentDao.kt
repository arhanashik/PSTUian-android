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
            + " WHERE " + ColumnNames.Student.FACULTY + "=:faculty"
            + " AND " + ColumnNames.Student.BATCH + "=:batch"
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    fun getAllLive(faculty: String, batch: String): LiveData<List<StudentEntity>>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    fun getAll(): List<StudentEntity>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY + "=:faculty"
            + " AND " + ColumnNames.Student.BATCH + "=:batch"
            + " ORDER BY " + ColumnNames.Student.ID + " ASC")
    fun getAll(faculty: String, batch: String): List<StudentEntity>

    @Query("SELECT * FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.ID + " = :id LIMIT 1")
    fun get(id: String): StudentEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(studentEntity: StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(studentEntities: List<StudentEntity>)

    @Update
    fun update(studentEntity: StudentEntity)

    @Delete
    fun delete(studentEntity: StudentEntity)

    @Query("DELETE FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY + "=:faculty")
    fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.STUDENT
            + " WHERE " + ColumnNames.Student.FACULTY + "=:faculty"
            + " AND " + ColumnNames.Student.BATCH + "=:batch")
    fun deleteAll(faculty: String, batch: String)

    @Query("DELETE FROM " + TableNames.STUDENT)
    fun deleteAll()
}