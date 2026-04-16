package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class EmployeeEntity(
    var id: Int,
    var name: String,
    var designation: String,
    var department: String?,
    var phone: String?,
    var address: String?,
    var facultyId: Int,
    var imageUrl: String?,
    var bio: String? = null,
    var blood: String? = null,
)
