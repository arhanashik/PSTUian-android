package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class Donation (
    val id: Int,
    val name: String?,
    val email: String?,
    val reference: String,
    val message: String?,
)
