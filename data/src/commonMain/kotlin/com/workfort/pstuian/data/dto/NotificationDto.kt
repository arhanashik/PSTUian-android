package com.workfort.pstuian.data.dto

import com.workfort.pstuian.featuredomain.model.NotificationEntity
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    var id: Int,
    var type: String,
    var title: String? = "",
    var message: String,
    var date: String? = "",
) {
    fun toEntity() = NotificationEntity(
        id = id,
        type = type,
        title = title,
        message = message,
        date = date
    )
}

fun NotificationEntity.toDto() = NotificationDto(
    id = id,
    type = type,
    title = title,
    message = message,
    date = date
)
