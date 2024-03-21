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
@Entity(tableName = TableNames.EMPLOYEE)
data class EmployeeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Employee.NAME) val name: String,
    @ColumnInfo(name = ColumnNames.Employee.DESIGNATION)
    val designation: String,
    @ColumnInfo(name = ColumnNames.Employee.DEPARTMENT) val department: String?,
    @ColumnInfo(name = ColumnNames.Employee.PHONE) val phone: String?,
    @ColumnInfo(name = ColumnNames.Employee.ADDRESS) val address: String?,
    @ColumnInfo(name = ColumnNames.Employee.FACULTY_ID)
    @SerializedName("faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Employee.IMAGE_URL)
    @SerializedName("image_url")
    val imageUrl: String?,
) : Parcelable