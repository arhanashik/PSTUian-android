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
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Teacher.NAME) val name: String,
    @ColumnInfo(name = ColumnNames.Teacher.DESIGNATION)
    val designation: String,
    @ColumnInfo(name = ColumnNames.Teacher.STATUS) val status: String?,
    @ColumnInfo(name = ColumnNames.Teacher.PHONE) val phone: String?,
    @ColumnInfo(name = ColumnNames.Teacher.LINKED_IN)
    @SerializedName("linked_in")
    val linkedIn: String?,
    @ColumnInfo(name = ColumnNames.Teacher.FB_LINK)
    @SerializedName("fb_link")
    val fbLink: String?,
    @ColumnInfo(name = ColumnNames.Teacher.ADDRESS) val address: String?,
    @ColumnInfo(name = ColumnNames.Teacher.EMAIL) val email: String?,
    @ColumnInfo(name = ColumnNames.Teacher.DEPARTMENT) val department: String,
    @ColumnInfo(name = ColumnNames.Teacher.FACULTY_ID)
    @SerializedName("faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Teacher.IMAGE_URL)
    @SerializedName("image_url")
    val imageUrl: String?
) : Parcelable