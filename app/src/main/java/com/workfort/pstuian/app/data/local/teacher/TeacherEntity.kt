package com.workfort.pstuian.app.data.local.teacher

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.TEACHER)
data class TeacherEntity(
    @PrimaryKey
    @ColumnInfo(name = ColumnNames.Teacher.ID)
    val id: Int,
    @ColumnInfo(name = ColumnNames.Teacher.NAME)
    var name: String,
    @ColumnInfo(name = ColumnNames.Teacher.DESIGNATION)
    val designation: String,
    @ColumnInfo(name = ColumnNames.Teacher.BIO)
    var bio: String?,
    @ColumnInfo(name = ColumnNames.Teacher.PHONE)
    var phone: String?,
    @ColumnInfo(name = ColumnNames.Teacher.LINKED_IN)
    @SerializedName("linked_in")
    var linkedIn: String?,
    @ColumnInfo(name = ColumnNames.Teacher.FB_LINK)
    @SerializedName("fb_link")
    var fbLink: String?,
    @ColumnInfo(name = ColumnNames.Teacher.ADDRESS)
    var address: String?,
    @ColumnInfo(name = ColumnNames.Teacher.EMAIL)
    var email: String?,
    @ColumnInfo(name = ColumnNames.Teacher.DEPARTMENT)
    var department: String,
    @ColumnInfo(name = ColumnNames.Teacher.BLOOD)
    var blood: String?,
    @ColumnInfo(name = ColumnNames.Teacher.FACULTY_ID)
    @SerializedName("faculty_id")
    var facultyId: Int,
    @ColumnInfo(name = ColumnNames.Teacher.IMAGE_URL)
    @SerializedName("image_url")
    var imageUrl: String?
) : Parcelable