package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.CourseEntity

@Entity(tableName = "course_schedule")
data class CourseDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "course_code")
    val courseCode: String,
    @ColumnInfo(name = "course_title")
    val courseTitle: String,
    @ColumnInfo(name = "credit_hour")
    val creditHour: String,
    @ColumnInfo(name = "faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = "status")
    val status: Int,
)

fun CourseDbEntity.toDomain() = CourseEntity(
    id = id,
    courseCode = courseCode,
    courseTitle = courseTitle,
    creditHour = creditHour,
    facultyId = facultyId,
    status = status,
)

fun CourseEntity.toDb() = CourseDbEntity(
    id = id,
    courseCode = courseCode,
    courseTitle = courseTitle,
    creditHour = creditHour,
    facultyId = facultyId,
    status = status,
)
