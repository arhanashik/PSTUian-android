package com.workfort.pstuian.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workfort.pstuian.data.local.database.entity.CourseDbEntity

@Dao
interface CourseDao {
    @Query("SELECT * FROM course_schedule ORDER BY course_code ASC")
    suspend fun getAll(): List<CourseDbEntity>

    @Query("SELECT * FROM course_schedule WHERE faculty_id=:facultyId ORDER BY course_code ASC")
    suspend fun getAll(facultyId: Int): List<CourseDbEntity>

    @Query("SELECT * FROM course_schedule WHERE id=:id LIMIT 1")
    suspend fun get(id: Int): CourseDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(courseEntity: CourseDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<CourseDbEntity>)

    @Update
    suspend fun update(courseEntity: CourseDbEntity)

    @Delete
    suspend fun delete(courseEntity: CourseDbEntity)

    @Query("DELETE FROM course_schedule WHERE faculty_id=:faculty")
    suspend fun deleteAll(faculty: Int)

    @Query("DELETE FROM course_schedule")
    suspend fun deleteAll()
}