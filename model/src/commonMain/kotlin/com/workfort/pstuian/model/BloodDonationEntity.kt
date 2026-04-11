package com.workfort.pstuian.model

@Parcelize
data class BloodDonationEntity(
    val id: Int,
    var requestId: Int?,
    var date: String,
    var info: String?,
    val userId: String,
    val userType: String,
    val name: String,
    val imageUrl: String?,
) : Parcelable
