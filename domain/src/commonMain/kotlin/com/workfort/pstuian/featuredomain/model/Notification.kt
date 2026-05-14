package com.workfort.pstuian.featuredomain.model

data class SystemNotification(
    val id: String,
    val title: String,
    val body: String,
    val linkText: String?,
    val link: String?,
    val showIn: SystemNotificationDisplayType,
    val isRead: Boolean,
    val requireSignIn: Boolean,
    val createdAt: Long,
)

enum class SystemNotificationDisplayType(val value: String) {
    ALERT("alert"),
    BANNER("banner"),
    NONE("none");

    companion object {
        fun create(value: String?) = entries.find { it.value == value } ?: NONE
    }
}

data class CustomNotification(
    val id: Int,
    val title: String,
    val body: String,
    val linkText: String?,
    val link: String?,
    val category: NotificationCategory,
    val fromUserId: Int,
    val toUserId: Int,
    val readAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
)

enum class NotificationCategory(val category: String) {
    DEFAULT("default"),
    BLOOD_DONATION("blood_donation"),
    NEWS("news"),
    HELP("help");

    companion object {
        fun create(category: String) = entries.firstOrNull { it.category == category } ?: DEFAULT
    }
}
