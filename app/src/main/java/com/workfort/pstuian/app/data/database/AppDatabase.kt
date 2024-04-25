package com.workfort.pstuian.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.workfort.pstuian.database.dao.BatchDao
import com.workfort.pstuian.database.dao.ConfigDao
import com.workfort.pstuian.database.dao.CourseDao
import com.workfort.pstuian.database.dao.EmployeeDao
import com.workfort.pstuian.database.dao.FacultyDao
import com.workfort.pstuian.database.dao.SliderDao
import com.workfort.pstuian.database.dao.StudentDao
import com.workfort.pstuian.database.dao.TeacherDao
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.ConfigEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.BuildConfig
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.R

@Database(
    entities = [
        ConfigEntity::class,
        SliderEntity::class,
        FacultyEntity::class,
        StudentEntity::class,
        TeacherEntity::class,
        BatchEntity::class,
        EmployeeEntity::class,
        CourseEntity::class,
    ],
    version = BuildConfig.VERSION_CODE_DB,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(): AppDatabase {
            if (!Companion::INSTANCE.isInitialized) {
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