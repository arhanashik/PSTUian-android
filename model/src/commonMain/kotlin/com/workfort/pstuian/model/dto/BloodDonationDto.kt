package com.workfort.pstuian.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.BloodDonationEntity

@Serializable
data class BloodDonationDto(
    val id: Int,
    @SerialName("request_id")
    var requestId: Int?,
    var date: String,
    var info: String?,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_type")
    val userType: String,
    val name: String,
    @SerialName("image_url")
    val imageUrl: String?,
) {
    fun toEntity() = BloodDonationEntity(
        id = id,
        requestId = requestId,
        date = date,
        info = info,
        userId = userId,
        userType = userType,
        name = name,
        imageUrl = imageUrl
    )
}

fun BloodDonationEntity.toDto() = BloodDonationDto(
    id = id,
    requestId = requestId,
    date = date,
    info = info,
    userId = userId,
    userType = userType,
    name = name,
    imageUrl = imageUrl
)
