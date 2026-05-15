package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckIn(
    val id: Int,
    val locationId: Int,
    val locationName: String,
    val locationImageUrl: String?,
    val count: Int,
    val privacy: String,
    val userId: Int,
    val userType: String,
    val userName: String,
    val userinfo: String,
    val userPhone: String?,
    val userImageUrl: String?,
    val date: String,
)
