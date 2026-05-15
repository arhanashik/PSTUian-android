package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.CheckIn
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckInDto(
    val id: Int,
    @SerialName("location_id")
    val locationId: Int,
    @SerialName("location_name")
    val locationName: String,
    @SerialName("location_image_url")
    val locationImageUrl: String?,
    val count: Int,
    val privacy: String,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("user_type")
    val userType: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("user_info")
    val userinfo: String,
    @SerialName("user_phone")
    val userPhone: String? = null,
    @SerialName("user_image_url")
    val userImageUrl: String? = null,
    val date: String,
) {
    fun toModel() = CheckIn(
        id = id,
        locationId = locationId,
        locationName = locationName,
        locationImageUrl = locationImageUrl,
        count = count,
        privacy = privacy,
        userId = userId,
        userType = userType,
        userName = userName,
        userinfo = userinfo,
        userPhone = userPhone,
        userImageUrl = userImageUrl,
        date = date,
    )
}
