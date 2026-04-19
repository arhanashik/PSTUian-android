package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class StudentDto(
    val userId: String,
    val name: String,
    val studentId: String,
    val reg: String,
    val phone: String?,
    val linkedIn: String?,
    val fbLink: String?,
    val blood: String?,
    val address: String?,
    val email: String,
    val batchId: Int,
    val session: String,
    val facultyId: Int,
    val imageUrl: String?,
    val cvLink: String?,
    val bio: String?,
) {

    fun toModel() = User.Student(
        userId = userId,
        name = name,
        email = email,
        facultyId = facultyId,
        phone = phone,
        address = address,
        bio = bio,
        blood = blood,
        imageUrl = imageUrl,
        studentId = studentId,
        reg = reg,
        linkedIn = linkedIn,
        fbLink = fbLink,
        batchId = batchId,
        session = session,
        cvLink = cvLink,
    )
}

fun User.Student.toDto() = StudentDto(
    userId = userId,
    name = name,
    email = email,
    facultyId = facultyId,
    phone = phone,
    address = address,
    bio = bio,
    blood = blood,
    imageUrl = imageUrl,
    studentId = studentId,
    reg = reg,
    linkedIn = linkedIn,
    fbLink = fbLink,
    batchId = batchId,
    session = session,
    cvLink = cvLink,
)
