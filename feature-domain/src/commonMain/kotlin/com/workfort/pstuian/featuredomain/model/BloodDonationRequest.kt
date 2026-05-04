package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class BloodDonationRequest(
    val id: Int,
    val bloodGroup: String,
    val beforeDate: String,
    val contacts: String,
    val info: String?,
    val userId: String,
    val userType: String,
    val name: String,
    val imageUrl: String?,
)
