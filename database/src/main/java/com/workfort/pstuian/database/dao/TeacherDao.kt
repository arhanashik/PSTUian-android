package com.workfort.pstuian.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.model.TeacherEntity

@Dao
interface TeacherDao {
    @Query("SELECT * FROM " + TableNames.TEACHER
            + " ORDER BY " + ColumnNames.Teacher.NAME + " ASC")
    suspend fun getAll(): List<TeacherEntity>

    @Query("SELECT * FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.FACULTY_ID + "=:facultyId"
            + " ORDER BY " + ColumnNames.Teacher.NAME + " ASC")
    suspend fun getAll(facultyId: Int): List<TeacherEntity>

    @Query("SELECT * FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.ID + "=:id")
    suspend fun get(id: Int): TeacherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teacherEntity: TeacherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<TeacherEntity>)

    @Update
    suspend fun update(teacherEntity: TeacherEntity)

    @Delete
    suspend fun delete(teacherEntity: TeacherEntity)

    @Query("DELETE FROM " + TableNames.TEACHER
            + " WHERE " + ColumnNames.Teacher.FACULTY_ID + "=:faculty")
    suspend fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.TEACHER)
    suspend fun deleteAll()
}