package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckInLocationDto (
    val id: Int,
    val name: String,
    val details: String?,
    @SerialName("image_url")
    val imageUrl : String?,
    val link: String?,
    val verified: Int,
    val count: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("user_type")
    val userType: String?,
) {
    fun toModel() = CheckInLocation(
        id = id,
        name = name,
        details = details,
        imageUrl = imageUrl,
        link = link,
        verified = verified,
        count = count,
        userId = userId,
        userType = userType
    )
}

fun CheckInLocation.toDto() = CheckInLocationDto(
    id = id,
    name = name,
    details = details,
    imageUrl = imageUrl,
    link = link,
    verified = verified,
    count = count,
    userId = userId,
    userType = userType
)
