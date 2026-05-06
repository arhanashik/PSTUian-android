package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class Donor (
    val id: Int,
    val name: String?,
    val info: String?,
    val email: String?,
    val reference: String
)
