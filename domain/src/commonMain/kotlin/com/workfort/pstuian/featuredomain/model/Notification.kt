package com.workfort.pstuian.featuredomain.model


interface Notification {
    val id: String
    val title: String
    val body: String
    val linkText: String?
    val link: String?
    val readAt: Long
    val createdAt: Long

    data class SystemNotification(
        override val id: String,
        override val title: String,
        override val body: String,
        override val linkText: String?,
        override val link: String?,
        override val readAt: Long,
        override val createdAt: Long,
        val showIn: SystemNotificationDisplayType,
        val requireSignIn: Boolean,
    ) : Notification

    data class CustomNotification(
        override val id: String,
        override val title: String,
        override val body: String,
        override val linkText: String?,
        override val link: String?,
        override val readAt: Long,
        override val createdAt: Long,
        val category: NotificationCategory,
        val fromUserId: Int,
        val fromUserType: UserType?,
        val fromUserName: String?,
        val fromUserImageUrl: String?,
        val toUserId: Int,
        val toUserType: UserType?,
        val updatedAt: Long,
    ) : Notification
}

enum class SystemNotificationDisplayType(val value: String) {
    ALERT("alert"),
    BANNER("banner"),
    NONE("none");

    companion object {
        fun create(value: String?) = entries.find { it.value == value } ?: NONE
    }
}

enum class NotificationCategory(val category: String) {
    DEFAULT("default"),
    NEW_FOLLOWER("new_follower"),
    NEW_FOLLOW_REQUEST("new_follow_request"),
    MESSAGE("message"),
    BLOOD_DONATION("blood_donation"),
    HELP("help");

    companion object {
        fun create(category: String) = entries.firstOrNull { it.category == category } ?: DEFAULT
    }
}
