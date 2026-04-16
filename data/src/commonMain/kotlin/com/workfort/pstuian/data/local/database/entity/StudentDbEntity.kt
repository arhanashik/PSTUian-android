package com.workfort.pstuian.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.workfort.pstuian.featuredomain.model.StudentEntity

@Entity(tableName = "student")
data class StudentDbEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "reg")
    val reg: String,
    @ColumnInfo(name = "phone")
    val phone: String?,
    @ColumnInfo(name = "linked_in")
    val linkedIn: String?,
    @ColumnInfo(name = "fb_link")
    val fbLink: String?,
    @ColumnInfo(name = "blood")
    val blood: String?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "batch_id")
    val batchId: Int,
    @ColumnInfo(name = "session")
    val session: String,
    @ColumnInfo(name = "faculty_id")
    val facultyId: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "cv_link")
    val cvLink: String?,
    @ColumnInfo(name = "bio")
    val bio: String?,
)

fun StudentDbEntity.toDomain() = StudentEntity(
    id = id,
    name = name,
    reg = reg,
    phone = phone,
    linkedIn = linkedIn,
    fbLink = fbLink,
    blood = blood,
    address = address,
    email = email,
    batchId = batchId,
    session = session,
    facultyId = facultyId,
    imageUrl = imageUrl,
    cvLink = cvLink,
    bio = bio,
)

fun StudentEntity.toDb() = StudentDbEntity(
    id = id,
    name = name,
    reg = reg,
    phone = phone,
    linkedIn = linkedIn,
    fbLink = fbLink,
    blood = blood,
    address = address,
    email = email,
    batchId = batchId,
    session = session,
    facultyId = facultyId,
    imageUrl = imageUrl,
    cvLink = cvLink,
    bio = bio,
)
