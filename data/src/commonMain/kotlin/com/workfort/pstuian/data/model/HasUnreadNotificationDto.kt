package com.workfort.pstuian.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HasUnreadNotificationDto(
    @SerialName("has_unread")
    val hasUnread: Boolean = false,
)
