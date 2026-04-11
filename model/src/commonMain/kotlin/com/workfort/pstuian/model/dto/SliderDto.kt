package com.workfort.pstuian.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.SliderEntity

@Serializable
data class SliderDto (
    val id: Int,
    val title: String?,
    @SerialName("image_url")
    val imageUrl: String?,
) {
    fun toEntity() = SliderEntity(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}

fun SliderEntity.toDto() = SliderDto(
    id = id,
    title = title,
    imageUrl = imageUrl
)
