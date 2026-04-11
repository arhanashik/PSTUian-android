package com.workfort.pstuian.model.dto

import kotlinx.serialization.Serializable
import com.workfort.pstuian.model.NotificationEntity

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
