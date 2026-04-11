package com.workfort.pstuian.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.StudentEntity

@Serializable
data class StudentDto(
    var id: Int,
    var name: String,
    var reg: String,
    var phone: String?,
    @SerialName("linked_in")
    var linkedIn: String?,
    @SerialName("fb_link")
    var fbLink: String?,
    var blood: String?,
    var address: String?,
    var email: String?,
    @SerialName("batch_id")
    var batchId: Int,
    var session: String,
    @SerialName("faculty_id")
    var facultyId: Int,
    @SerialName("image_url")
    var imageUrl: String?,
    @SerialName("cv_link")
    var cvLink: String?,
    var bio: String?,
) {
    fun toEntity() = StudentEntity(
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
        bio = bio
    )
}

fun StudentEntity.toDto() = StudentDto(
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
    bio = bio
)
