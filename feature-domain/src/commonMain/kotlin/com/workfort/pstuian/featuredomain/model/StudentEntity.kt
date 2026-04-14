package com.workfort.pstuian.featuredomain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.appconstant.ColumnNames

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
    var linkedIn: String?,
    @ColumnInfo(name = ColumnNames.Student.FB_LINK)
    var fbLink: String?,
    @ColumnInfo(name = ColumnNames.Student.BLOOD)
    var blood: String?,
    @ColumnInfo(name = ColumnNames.Student.ADDRESS)
    var address: String?,
    @ColumnInfo(name = ColumnNames.Student.EMAIL)
    var email: String?,
    @ColumnInfo(name = ColumnNames.Student.BATCH_ID)
    var batchId: Int,
    @ColumnInfo(name = ColumnNames.Student.SESSION)
    var session: String,
    @ColumnInfo(name = ColumnNames.Student.FACULTY_ID)
    var facultyId: Int,
    @ColumnInfo(name = ColumnNames.Student.IMAGE_URL)
    var imageUrl: String?,
    @ColumnInfo(name = ColumnNames.Student.CV_LINK)
    var cvLink: String?,
    @ColumnInfo(name = ColumnNames.Student.BIO)
    var bio: String?,
) {
    fun toEntity() = this
}
