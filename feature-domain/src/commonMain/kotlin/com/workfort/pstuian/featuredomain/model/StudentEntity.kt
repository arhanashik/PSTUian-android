package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentEntity(
    var id: Int,
    var name: String,
    var reg: String,
    var phone: String?,
    var linkedIn: String?,
    var fbLink: String?,
    var blood: String?,
    var address: String?,
    var email: String?,
    var batchId: Int,
    var session: String,
    var facultyId: Int,
    var imageUrl: String?,
    var cvLink: String?,
    var bio: String?,
)
