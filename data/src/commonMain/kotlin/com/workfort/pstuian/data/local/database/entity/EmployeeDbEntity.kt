package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.EmployeeEntity

@Entity(tableName = "employee")
data class EmployeeDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "designation")
    val designation: String,
    @ColumnInfo(name = "department")
    val department: String?,
    @ColumnInfo(name = "phone")
    val phone: String?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "bio")
    val bio: String?,
    @ColumnInfo(name = "blood")
    val blood: String?,
)

fun EmployeeDbEntity.toDomain() = EmployeeEntity(
    id = id,
    name = name,
    designation = designation,
    department = department,
    phone = phone,
    address = address,
    facultyId = facultyId,
    imageUrl = imageUrl,
    bio = bio,
    blood = blood,
)

fun EmployeeEntity.toDb() = EmployeeDbEntity(
    id = id,
    name = name,
    designation = designation,
    department = department,
    phone = phone,
    address = address,
    facultyId = facultyId,
    imageUrl = imageUrl,
    bio = bio,
    blood = blood,
)
