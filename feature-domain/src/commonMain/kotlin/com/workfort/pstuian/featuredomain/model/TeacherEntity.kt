package com.workfort.pstuian.featuredomain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import com.workfort.pstuian.appconstant.TableNames
import com.workfort.pstuian.appconstant.ColumnNames

@Serializable
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
    var linkedIn: String?,
    @ColumnInfo(name = ColumnNames.Teacher.FB_LINK)
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
    var facultyId: Int,
    @ColumnInfo(name = ColumnNames.Teacher.IMAGE_URL)
    var imageUrl: String?,
    @ColumnInfo(name = "description")
    var description: String? = null,
) {
    fun toEntity() = this
}
