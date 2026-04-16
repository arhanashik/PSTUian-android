package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.TeacherEntity

@Entity(tableName = "teacher")
data class TeacherDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "designation")
    val designation: String,
    @ColumnInfo(name = "bio")
    val bio: String?,
    @ColumnInfo(name = "phone")
    val phone: String?,
    @ColumnInfo(name = "linked_in")
    val linkedIn: String?,
    @ColumnInfo(name = "fb_link")
    val fbLink: String?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "department")
    val department: String,
    @ColumnInfo(name = "blood")
    val blood: String?,
    @ColumnInfo(name = "faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "description")
    val description: String?,
)

fun TeacherDbEntity.toDomain() = TeacherEntity(
    id = id,
    name = name,
    designation = designation,
    bio = bio,
    phone = phone,
    linkedIn = linkedIn,
    fbLink = fbLink,
    address = address,
    email = email,
    department = department,
    blood = blood,
    facultyId = facultyId,
    imageUrl = imageUrl,
    description = description,
)

fun TeacherEntity.toDb() = TeacherDbEntity(
    id = id,
    name = name,
    designation = designation,
    bio = bio,
    phone = phone,
    linkedIn = linkedIn,
    fbLink = fbLink,
    address = address,
    email = email,
    department = department,
    blood = blood,
    facultyId = facultyId,
    imageUrl = imageUrl,
    description = description,
)
