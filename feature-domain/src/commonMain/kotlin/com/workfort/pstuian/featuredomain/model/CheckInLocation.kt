package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckInLocation (
    val id: Int,
    val name: String,
    val details: String?,
    val imageUrl : String?,
    val link: String?,
    val verified: Int,
    val count: Int,
    val userId: Int,
    val userType: String?
)
