package com.workfort.pstuian.app.data.local.student

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.STUDENT)
data class StudentEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Student.NAME) var name: String,
    @ColumnInfo(name = ColumnNames.Student.REG) val reg: String,
    @ColumnInfo(name = ColumnNames.Student.PHONE) val phone: String?,
    @ColumnInfo(name = ColumnNames.Student.LINKED_IN)
    @SerializedName("linked_in")
    val linkedIn: String?,
    @ColumnInfo(name = ColumnNames.Student.FB_LINK)
    @SerializedName("fb_link")
    val fbLink: String?,
    @ColumnInfo(name = ColumnNames.Student.BLOOD) val blood: String?,
    @ColumnInfo(name = ColumnNames.Student.ADDRESS) val address: String?,
    @ColumnInfo(name = ColumnNames.Student.EMAIL) val email: String?,
    @ColumnInfo(name = ColumnNames.Student.BATCH_ID) val batchId: Int,
    @ColumnInfo(name = ColumnNames.Student.SESSION) val session: String,
    @ColumnInfo(name = ColumnNames.Student.FACULTY_ID)
    @SerializedName("faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Student.IMAGE_URL)
    @SerializedName("image_url")
    var imageUrl: String?,
    @ColumnInfo(name = ColumnNames.Student.CV_LINK)
    @SerializedName("cv_link")
    val cvLink: String?
) : Parcelable