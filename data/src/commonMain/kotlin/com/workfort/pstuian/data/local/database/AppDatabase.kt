package com.workfort.pstuian.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.workfort.pstuian.data.local.database.dao.BatchDao
import com.workfort.pstuian.data.local.database.dao.ConfigDao
import com.workfort.pstuian.data.local.database.dao.CourseDao
import com.workfort.pstuian.data.local.database.dao.EmployeeDao
import com.workfort.pstuian.data.local.database.dao.FacultyDao
import com.workfort.pstuian.data.local.database.dao.SliderDao
import com.workfort.pstuian.data.local.database.dao.StudentDao
import com.workfort.pstuian.data.local.database.dao.TeacherDao
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.ConfigEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.EmployeeEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity

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
