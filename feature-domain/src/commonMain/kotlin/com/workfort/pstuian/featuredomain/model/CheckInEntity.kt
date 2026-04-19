package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckInEntity(
    val id: Int,
    val locationId: Int,
    val locationName: String,
    val locationImageUrl: String?,
    val count: Int,
    val privacy: String,
    val userId: String,
    val userType: String,
    val name: String,
    val batch: String,
    val phone: String?,
    val imageUrl: String?,
    val date: String
)
