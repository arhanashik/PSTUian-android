package com.workfort.pstuian.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.appconstant.ColumnNames
import com.workfort.pstuian.appconstant.TableNames

@Entity(tableName = TableNames.EMPLOYEE)
data class EmployeeEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = ColumnNames.Employee.NAME)
    val name: String,
    @ColumnInfo(name = ColumnNames.Employee.DESIGNATION)
    val designation: String,
    @ColumnInfo(name = ColumnNames.Employee.DEPARTMENT)
    val department: String?,
    @ColumnInfo(name = ColumnNames.Employee.PHONE)
    val phone: String?,
    @ColumnInfo(name = ColumnNames.Employee.ADDRESS)
    val address: String?,
    @ColumnInfo(name = ColumnNames.Employee.FACULTY_ID)
    val facultyId: Int,
    @ColumnInfo(name = ColumnNames.Employee.IMAGE_URL)
    val imageUrl: String?,
)
