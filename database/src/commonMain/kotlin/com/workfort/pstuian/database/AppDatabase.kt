package com.workfort.pstuian.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.workfort.pstuian.database.dao.*
import com.workfort.pstuian.model.*

@Database(
    entities = [
        ConfigEntity::class,
        FacultyEntity::class,
        BatchEntity::class,
        TeacherEntity::class,
        CourseEntity::class,
        EmployeeEntity::class,
        StudentEntity::class,
        SliderEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun facultyDao(): FacultyDao
    abstract fun batchDao(): BatchDao
    abstract fun teacherDao(): TeacherDao
    abstract fun courseDao(): CourseDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun studentDao(): StudentDao
    abstract fun sliderDao(): SliderDao
}

// Room KMP requires a way to instantiate the database
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
