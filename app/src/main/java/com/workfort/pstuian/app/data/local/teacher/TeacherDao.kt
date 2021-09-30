package com.workfort.pstuian.app.data.local.teacher

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface TeacherDao {
    @Query("SELECT * FROM " + TableNames.TEACHER
            + " ORDER BY " + ColumnNames.Teacher.NAME + " ASC")
    fun getAllLive(): LiveData<List<TeacherEntity>>

    @Query("SELECT * FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.FACULTY + "=:faculty"
            + " ORDER BY " + ColumnNames.Teacher.NAME + " ASC")
    fun getAllLive(faculty: String): LiveData<List<TeacherEntity>>

    @Query("SELECT * FROM " + TableNames.TEACHER
            + " ORDER BY " + ColumnNames.Teacher.NAME + " ASC")
    fun getAll(): List<TeacherEntity>

    @Query("SELECT * FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.FACULTY + "=:faculty"
            + " ORDER BY " + ColumnNames.Teacher.NAME + " ASC")
    fun getAll(faculty: String): List<TeacherEntity>

    @Query("SELECT * FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.ID + "=:id LIMIT 1")
    fun get(id: String): TeacherEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(teacherEntity: TeacherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(teacherEntities: List<TeacherEntity>)

    @Update
    fun update(teacherEntity: TeacherEntity)

    @Delete
    fun delete(teacherEntity: TeacherEntity)

    @Query("DELETE FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.FACULTY + "=:faculty")
    fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.TEACHER)
    fun deleteAll()
}