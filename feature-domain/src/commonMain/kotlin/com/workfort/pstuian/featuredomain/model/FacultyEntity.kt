package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class FacultyEntity (
    var id: Int,
    var shortTitle: String,
    var title: String,
    var icon: String?,
)
