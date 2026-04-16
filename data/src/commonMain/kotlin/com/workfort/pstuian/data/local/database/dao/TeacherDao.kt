package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.TeacherDbEntity

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teacher ORDER BY name ASC")
    suspend fun getAll(): List<TeacherDbEntity>

    @Query("SELECT * FROM teacher WHERE faculty_id=:facultyId ORDER BY name ASC")
    suspend fun getAll(facultyId: Int): List<TeacherDbEntity>

    @Query("SELECT * FROM teacher WHERE id=:id")
    suspend fun get(id: Int): TeacherDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teacherEntity: TeacherDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<TeacherDbEntity>)

    @Update
    suspend fun update(teacherEntity: TeacherDbEntity)

    @Delete
    suspend fun delete(teacherEntity: TeacherDbEntity)

    @Query("DELETE FROM teacher WHERE faculty_id=:faculty")
    suspend fun deleteAll(faculty: Int)

    @Query("DELETE FROM teacher")
    suspend fun deleteAll()
}