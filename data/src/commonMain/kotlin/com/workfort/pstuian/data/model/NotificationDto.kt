package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.NotificationCategory
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import com.workfort.pstuian.featuredomain.model.UserType
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.serialization.SerialName
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
    @SerialName("link_text")
    val linkText: String? = null,
    val link: String? = null,
    val category: String = "",
    @SerialName("from_user_id")
    val fromUserId: Int = 0,
    @SerialName("from_user_type")
    val fromUserType: String = "",
    @SerialName("to_user_id")
    val toUserId: Int = 0,
    @SerialName("to_user_type")
    val toUserType: String = "",
    @SerialName("read_at")
    val readAt: Long = 0,
    @SerialName("created_at")
    val createdAt: Long = 0,
    @SerialName("updated_at")
    val updatedAt: Long = 0,
) {
    fun toModel() = Notification.CustomNotification(
        id = id.toString(),
        title = title,
        body = body,
        linkText = linkText,
        link = link,
        category = NotificationCategory.create(category),
        fromUserId = fromUserId,
        fromUserType = UserType.fromType(fromUserType),
        toUserId = toUserId,
        toUserType = UserType.fromType(toUserType),
        readAt = readAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

@Serializable
data class UserSystemNotificationReadDto(
    val readAt: Timestamp = Timestamp(0, 0),
)
