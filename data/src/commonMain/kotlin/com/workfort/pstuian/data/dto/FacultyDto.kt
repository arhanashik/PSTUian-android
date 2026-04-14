package com.workfort.pstuian.data.dto

import com.workfort.pstuian.featuredomain.model.FacultyEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FacultyDto(
    val id: Int,
    @SerialName("short_title")
    val shortTitle: String,
    val title: String,
    val icon: String?,
) {
    fun toEntity() = FacultyEntity(
        id = id,
        shortTitle = shortTitle,
        title = title,
        icon = icon
    )
}

fun FacultyEntity.toDto() = FacultyDto(
    id = id,
    shortTitle = shortTitle,
    title = title,
    icon = icon
)
