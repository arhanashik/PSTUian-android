package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class Faculty (
    var id: Int,
    var shortTitle: String,
    var title: String,
    var icon: String?,
)
