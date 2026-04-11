package com.workfort.pstuian.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.CheckInLocationEntity

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
    @SerialName("user_name")
    val userName: String?
) {
    fun toEntity() = CheckInLocationEntity(
        id = id,
        name = name,
        details = details,
        imageUrl = imageUrl,
        link = link,
        verified = verified,
        count = count,
        userId = userId,
        userName = userName
    )
}

fun CheckInLocationEntity.toDto() = CheckInLocationDto(
    id = id,
    name = name,
    details = details,
    imageUrl = imageUrl,
    link = link,
    verified = verified,
    count = count,
    userId = userId,
    userName = userName
)
