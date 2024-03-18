package com.workfort.pstuian.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.COURSE_SCHEDULE)
data class CourseEntity (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Course.COURSE_CODE)
    @SerializedName("course_code")
    val courseCode: String,
    @ColumnInfo(name = ColumnNames.Course.COURSE_TITLE)
    @SerializedName("course_title")
    val courseTitle: String,
    @ColumnInfo(name = ColumnNames.Course.CREDIT_HOUR)
    @SerializedName("credit_hour")
    val creditHour: String,
    @ColumnInfo(name = ColumnNames.Course.FACULTY_ID)
    @SerializedName("faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Course.STATUS) val status: Int,
) : Parcelable