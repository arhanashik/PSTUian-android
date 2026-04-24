package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Faculty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FacultyDto(
    val id: Int,
    @SerialName("short_title")
    val shortTitle: String,
    val title: String,
    val icon: String? = null,
) {
    fun toModel() = Faculty(
        id = id,
        shortTitle = shortTitle,
        title = title,
        icon = icon
    )
}

fun Faculty.toDto() = FacultyDto(
    id = id,
    shortTitle = shortTitle,
    title = title,
    icon = icon
)
