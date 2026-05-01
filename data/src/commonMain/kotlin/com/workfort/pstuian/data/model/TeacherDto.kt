package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherDto(
    @SerialName("auth_user_id")
    val authUserId: String,
    val id: Int,
    var name: String,
    val designation: String,
    var bio: String?,
    var phone: String?,
    @SerialName("linked_in")
    var linkedIn: String?,
    @SerialName("fb_link")
    var fbLink: String?,
    var address: String?,
    var email: String,
    var department: String,
    var blood: String?,
    @SerialName("faculty_id")
    var facultyId: Int,
    @SerialName("image_url")
    var imageUrl: String?,
) {
    fun toModel() = User.Teacher(
        authUserId = authUserId,
        userId = id,
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
    )
}

fun User.Teacher.toDto() = TeacherDto(
    authUserId = authUserId,
    id = userId,
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
    imageUrl = imageUrl
)
