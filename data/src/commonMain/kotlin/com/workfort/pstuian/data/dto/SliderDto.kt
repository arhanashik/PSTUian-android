package com.workfort.pstuian.data.dto

import com.workfort.pstuian.featuredomain.model.SliderEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
