package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeacherDto(
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
    fun toEntity() = TeacherEntity(
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
        imageUrl = imageUrl
    )

    fun toModel() = User.Teacher(
        userId = id.toString(),
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
}

fun TeacherEntity.toDto() = TeacherDto(
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
    imageUrl = imageUrl
)
