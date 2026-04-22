package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Slider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SliderDto (
    val id: Int,
    val title: String?,
    @SerialName("image_url")
    val imageUrl: String?,
) {
    fun toModel() = Slider(
        id = id,
        title = title,
        imageUrl = imageUrl
    )
}

fun Slider.toDto() = SliderDto(
    id = id,
    title = title,
    imageUrl = imageUrl
)
