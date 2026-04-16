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
import com.workfort.pstuian.data.local.database.entity.BatchDbEntity
import com.workfort.pstuian.data.local.database.entity.ConfigDbEntity
import com.workfort.pstuian.data.local.database.entity.CourseDbEntity
import com.workfort.pstuian.data.local.database.entity.EmployeeDbEntity
import com.workfort.pstuian.data.local.database.entity.FacultyDbEntity
import com.workfort.pstuian.data.local.database.entity.SliderDbEntity
import com.workfort.pstuian.data.local.database.entity.StudentDbEntity
import com.workfort.pstuian.data.local.database.entity.TeacherDbEntity

@Database(
    entities = [
        ConfigDbEntity::class,
        FacultyDbEntity::class,
        BatchDbEntity::class,
        TeacherDbEntity::class,
        CourseDbEntity::class,
        EmployeeDbEntity::class,
        StudentDbEntity::class,
        SliderDbEntity::class,
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
