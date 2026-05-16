package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.firestore.FirestoreSystemNotificationDataSource
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.featuredomain.repository.SystemNotificationRepository
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SystemNotificationRepositoryImpl(
    private val systemNotificationDataSource: FirestoreSystemNotificationDataSource,
    private val sharedPrefRepository: SharedPrefRepository,
) : SystemNotificationRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _systemNotifications = MutableStateFlow<List<Notification.SystemNotification>>(emptyList())
    private var systemNotificationsJobStarted = false

    override fun observeSystemNotifications(authUserId: String): Flow<List<Notification.SystemNotification>> {
        startObservingSystemNotifications(authUserId)
        return _systemNotifications
    }

    override fun observeUnreadSystemNotifications(): Flow<List<Notification.SystemNotification>> {
        return _systemNotifications.map { notifications ->
            notifications.filter { it.readAt == 0L }
        }
    }

    override fun observeNewSystemNotification(type: SystemNotificationDisplayType): Flow<Notification.SystemNotification> {
        val sharedPrefKey = type.getClosedAtSharedPrefKey() ?: return emptyFlow()
        val lastReadTimestamp = sharedPrefRepository.getDouble(sharedPrefKey)

        return _systemNotifications.map { notifications ->
            notifications.firstOrNull { it.createdAt > lastReadTimestamp && it.showIn == type && it.readAt == 0L }
        }.filterNotNull()
    }

    override suspend fun markSystemNotificationAsRead(authUserId: String, notificationId: String): DomainResult<Unit> {
        return systemNotificationDataSource.markSystemNotificationAsRead(authUserId, notificationId)
            .toDomainResult()
    }

    override fun updateSystemNotificationClosedTimestamp(type: SystemNotificationDisplayType) {
        val sharedPrefKey = type.getClosedAtSharedPrefKey() ?: return
        sharedPrefRepository.putDouble(sharedPrefKey, Timestamp.now().toMilliseconds())
    }

    private fun startObservingSystemNotifications(userId: String) {
        if (systemNotificationsJobStarted) return
        systemNotificationsJobStarted = true

        combine(
            systemNotificationDataSource.observeSystemNotifications(),
            systemNotificationDataSource.observeReadSystemNotificationIds(userId),
        ) { networkResult, readAtByNotificationId ->
            networkResult.toDomainResult().onSuccess { pairs ->
                val notifications = pairs.map { (id, dto) ->
                    dto.toModel(id = id, readAt = readAtByNotificationId[id] ?: 0L)
                }.sortedByDescending { it.createdAt }
                _systemNotifications.update { notifications }
            }
        }.launchIn(scope)
    }

    private fun SystemNotificationDisplayType.getClosedAtSharedPrefKey() = when (this) {
        SystemNotificationDisplayType.ALERT -> SharedPrefKey.SYSTEM_NOTIFICATION_ALERT_CLOSED_AT
        SystemNotificationDisplayType.BANNER -> SharedPrefKey.SYSTEM_NOTIFICATION_BANNER_CLOSED_AT
        SystemNotificationDisplayType.NONE -> null
    }
}
