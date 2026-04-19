package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class TeacherEntity(
    var id: Int,
    var name: String,
    var designation: String,
    var bio: String?,
    var phone: String?,
    var linkedIn: String?,
    var fbLink: String?,
    var address: String?,
    var email: String,
    var department: String,
    var blood: String?,
    var facultyId: Int,
    var imageUrl: String?,
    var description: String? = null,
)
