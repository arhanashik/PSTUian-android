package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CustomNotificationRepository
import com.workfort.pstuian.util.DateTimeUtil

class CustomNotificationRepositoryImpl(
    private val helper: NotificationApiHelper,
    private val dateTimeUtil: DateTimeUtil,
) : CustomNotificationRepository {

    private val notificationsCache = mutableMapOf<Int, List<Notification.CustomNotification>>()

    override suspend fun getAll(
        userType: String,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<Notification.CustomNotification>> {
        if (forceRefresh) notificationsCache.clear()

        val cache = notificationsCache[page]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getAll(userType, page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { notificationsCache[page] = it }
    }

    override suspend fun hasUnreadCustomNotifications(userType: String): DomainResult<Boolean> {
        return helper.hasUnreadCustomNotifications(userType).toDomainResult()
    }

    override suspend fun markCustomNotificationAsRead(
        notification: Notification.CustomNotification,
    ): DomainResult<Notification.CustomNotification> {
        return helper.markCustomNotificationAsRead(notification.id)
            .toDomainResult()
            .map {
                val readAt = dateTimeUtil.getTimeInMillisNow()
                val updatedNotification = notification.copy(
                    readAt = readAt,
                    updatedAt = readAt,
                )
                replaceNotificationInCache(updatedNotification)
                updatedNotification
            }
    }

    private fun replaceNotificationInCache(updatedNotification: Notification.CustomNotification) {
        notificationsCache.entries.forEach { (key, notifications) ->
            val index = notifications.indexOfFirst { it.id == updatedNotification.id }
            if (index >= 0) {
                notificationsCache[key] = notifications.toMutableList().apply {
                    this[index] = updatedNotification
                }
            }
        }
    }
}

private fun Notification.CustomNotification.withResolvedSenderImage(apiBaseUrl: String) =
    copy(fromUserImageUrl = resolveAgainstApiBase(fromUserImageUrl, apiBaseUrl))

/**
 * Coil loads absolute URLs. The API sometimes returns paths relative to the API origin.
 * Authenticated images must be fetched with the same Ktor [HttpClient] as JSON (auth headers); see app Coil setup.
 */
private fun resolveAgainstApiBase(raw: String?, apiBaseUrl: String): String? {
    val trimmed = raw?.trim()?.takeIf { it.isNotEmpty() } ?: return null
    return when {
        trimmed.startsWith("http://", ignoreCase = true) ||
            trimmed.startsWith("https://", ignoreCase = true) -> trimmed
        trimmed.startsWith("//") -> "https:$trimmed"
        else -> {
            val base = apiBaseUrl.trimEnd('/')
            val path = trimmed.trimStart('/')
            "$base/$path"
        }
    }
}
