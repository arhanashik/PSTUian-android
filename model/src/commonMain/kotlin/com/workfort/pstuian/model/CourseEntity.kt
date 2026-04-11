package com.workfort.pstuian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames

@Entity(tableName = TableNames.COURSE_SCHEDULE)
data class CourseEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Course.COURSE_CODE)
    val courseCode: String,
    @ColumnInfo(name = ColumnNames.Course.COURSE_TITLE)
    val courseTitle: String,
    @ColumnInfo(name = ColumnNames.Course.CREDIT_HOUR)
    val creditHour: String,
    @ColumnInfo(name = ColumnNames.Course.FACULTY_ID)
    val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Course.STATUS) val status: Int,
)
