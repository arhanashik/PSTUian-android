package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class DonorEntity (
    val id: Int,
    val name: String?,
    val info: String?,
    val email: String?,
    val reference: String
)
