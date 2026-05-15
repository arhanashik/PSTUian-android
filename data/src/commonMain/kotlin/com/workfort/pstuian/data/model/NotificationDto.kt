package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.NotificationCategory
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.serialization.Serializable

@Serializable
data class SystemNotificationDto(
    val title: String = "",
    val body: String = "",
    val linkText: String? = null,
    val link: String? = null,
    val showIn: String = "",
    val requireSignIn: Boolean = false,
    val createdAt: Timestamp = Timestamp(0, 0),
) {
    fun toModel(id: String, readAt: Long) = Notification.SystemNotification(
        id = id,
        title = title,
        body = body,
        linkText = linkText,
        link = link,
        showIn = SystemNotificationDisplayType.create(showIn),
        readAt = readAt,
        requireSignIn = requireSignIn,
        createdAt = createdAt.toMilliseconds().toLong(),
    )
}

@Serializable
data class CustomNotificationDto(
    val id: Int,
    val title: String,
    val body: String,
    val linkText: String? = null,
    val link: String? = null,
    val category: String = "",
    val fromUserId: Int = 0,
    val toUserId: Int = 0,
    val readAt: Long = 0,
    val createdAt: Long = 0,
    val updatedAt: Long = 0,
) {
    fun toModel(id: Int) = Notification.CustomNotification(
        id = id.toString(),
        title = title,
        body = body,
        linkText = linkText,
        link = link,
        category = NotificationCategory.create(category),
        fromUserId = fromUserId,
        toUserId = toUserId,
        readAt = readAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
