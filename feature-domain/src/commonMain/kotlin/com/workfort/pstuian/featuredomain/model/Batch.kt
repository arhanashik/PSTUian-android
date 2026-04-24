package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class Batch (
    var id: Int,
    var name: String,
    var title: String?,
    var session: String,
    var facultyId: Int,
    var totalStudent: Int,
    var registeredStudent: Int,
)
