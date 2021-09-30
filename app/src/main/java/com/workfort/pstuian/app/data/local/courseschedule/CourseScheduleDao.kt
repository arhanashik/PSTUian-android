package com.workfort.pstuian.app.data.local.courseschedule

import androidx.lifecycle.LiveData
import androidx.room.*
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames

@Dao
interface CourseScheduleDao {
    @Query("SELECT * FROM " + TableNames.COURSE_SCHEDULE
            + " ORDER BY " + ColumnNames.CourseSchedule.COURSE_CODE + " ASC")
    fun getAllLive(): LiveData<List<CourseScheduleEntity>>

    @Query("SELECT * FROM " + TableNames.COURSE_SCHEDULE
            + " WHERE " + ColumnNames.CourseSchedule.FACULTY + "=:faculty"
            + " ORDER BY " + ColumnNames.CourseSchedule.COURSE_CODE + " ASC")
    fun getAllLive(faculty: String): LiveData<List<CourseScheduleEntity>>

    @Query("SELECT * FROM " + TableNames.COURSE_SCHEDULE
            + " ORDER BY " + ColumnNames.CourseSchedule.COURSE_CODE + " ASC")
    fun getAll(): List<CourseScheduleEntity>

    @Query("SELECT * FROM " + TableNames.COURSE_SCHEDULE
            + " WHERE " + ColumnNames.CourseSchedule.FACULTY + "=:faculty"
            + " ORDER BY " + ColumnNames.CourseSchedule.COURSE_CODE + " ASC")
    fun getAll(faculty: String): List<CourseScheduleEntity>

    @Query("SELECT * FROM " + TableNames.COURSE_SCHEDULE
            + " WHERE " + ColumnNames.CourseSchedule.ID + "=:id LIMIT 1")
    fun get(id: String): CourseScheduleEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(courseScheduleEntity: CourseScheduleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(courseScheduleEntity: List<CourseScheduleEntity>)

    @Update
    fun update(courseScheduleEntity: CourseScheduleEntity)

    @Delete
    fun delete(courseScheduleEntity: CourseScheduleEntity)

    @Query("DELETE FROM " + TableNames.COURSE_SCHEDULE
            + " WHERE " + ColumnNames.CourseSchedule.FACULTY + "=:faculty")
    fun deleteAll(faculty: String)

    @Query("DELETE FROM " + TableNames.COURSE_SCHEDULE)
    fun deleteAll()
}