package com.workfort.pstuian.app.data.local.student

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.workfort.pstuian.app.data.local.batch.BatchEntity
import com.workfort.pstuian.app.data.local.database.ColumnNames
import com.workfort.pstuian.app.data.local.database.TableNames
import com.workfort.pstuian.app.data.local.faculty.FacultyEntity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TableNames.STUDENT)
data class StudentEntity(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = ColumnNames.Student.NAME)
    var name: String,
    @ColumnInfo(name = ColumnNames.Student.REG)
    var reg: String,
    @ColumnInfo(name = ColumnNames.Student.PHONE)
    var phone: String?,
    @ColumnInfo(name = ColumnNames.Student.LINKED_IN)
    @SerializedName("linked_in")
    var linkedIn: String?,
    @ColumnInfo(name = ColumnNames.Student.FB_LINK)
    @SerializedName("fb_link")
    var fbLink: String?,
    @ColumnInfo(name = ColumnNames.Student.BLOOD)
    var blood: String?,
    @ColumnInfo(name = ColumnNames.Student.ADDRESS)
    var address: String?,
    @ColumnInfo(name = ColumnNames.Student.EMAIL)
    var email: String?,
    @SerializedName("batch_id")
    @ColumnInfo(name = ColumnNames.Student.BATCH_ID)
    var batchId: Int,
    @ColumnInfo(name = ColumnNames.Student.SESSION)
    var session: String,
    @ColumnInfo(name = ColumnNames.Student.FACULTY_ID)
    @SerializedName("faculty_id")
    var facultyId: Int,
    @ColumnInfo(name = ColumnNames.Student.IMAGE_URL)
    @SerializedName("image_url")
    var imageUrl: String?,
    @ColumnInfo(name = ColumnNames.Student.CV_LINK)
    @SerializedName("cv_link")
    var cvLink: String?,
    @ColumnInfo(name = ColumnNames.Student.BIO)
    var bio: String?,
) : Parcelable