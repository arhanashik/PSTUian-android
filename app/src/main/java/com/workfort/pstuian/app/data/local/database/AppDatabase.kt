package com.workfort.pstuian.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.R
import com.workfort.pstuian.app.data.local.batch.BatchDao
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.config.ConfigDao
import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.course.CourseDao
import com.workfort.pstuian.app.data.local.course.CourseEntity
import com.workfort.pstuian.app.data.local.employee.EmployeeDao
import com.workfort.pstuian.app.data.local.employee.EmployeeEntity
import com.workfort.pstuian.app.data.local.faculty.FacultyDao
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import com.workfort.pstuian.app.data.local.slider.SliderDao
import com.workfort.pstuian.app.data.local.slider.SliderEntity
import com.workfort.pstuian.app.data.local.student.StudentDao
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherDao
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

@Database(entities = [
    ConfigEntity::class,
    SliderEntity::class,
    FacultyEntity::class,
    StudentEntity::class,
    TeacherEntity::class,
    BatchEntity::class,
    EmployeeEntity::class,
    CourseEntity::class], version = BuildConfig.VERSION_CODE_DB, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class.java) {
                    val context = PstuianApp.getBaseApplicationContext()
                    INSTANCE = Room.databaseBuilder(
                        context, AppDatabase::class.java, context.getString(R.string.db_name)
                    ).addMigrations(
                        MIGRATION_1_2
                    ).build()
                }
            }

            return INSTANCE
        }
    }

    abstract fun configDao(): ConfigDao
    abstract fun sliderDao(): SliderDao
    abstract fun facultyDao(): FacultyDao
    abstract fun studentDao(): StudentDao
    abstract fun teacherDao(): TeacherDao
    abstract fun batchDao(): BatchDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun courseScheduleDao(): CourseDao
}