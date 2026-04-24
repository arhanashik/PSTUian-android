package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentDto(
    @SerialName("user_id")
    val userId: String,
    val name: String,
    @SerialName("id")
    val studentId: Int,
    val reg: String,
    @SerialName("faculty_id")
    val facultyId: Int,
    @SerialName("batch_id")
    val batchId: Int,
    val session: String,
    val phone: String?,
    @SerialName("linked_in")
    val linkedIn: String?,
    @SerialName("fb_link")
    val fbLink: String?,
    val blood: String?,
    val address: String?,
    val email: String,
    @SerialName("image_url")
    val imageUrl: String?,
    @SerialName("cv_link")
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
