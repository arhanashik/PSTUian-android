package com.workfort.pstuian.ui.notification.customnotification

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.NotificationCategory
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CustomNotificationRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.notification.common.NotificationDisplayDataMapper
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationMessageState
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiEvent
import com.workfort.pstuian.ui.notification.customnotification.state.CustomNotificationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CustomNotificationViewModel(
    private val customNotificationRepository: CustomNotificationRepository,
    private val sharedScreenData: SharedScreenData,
    private val notificationDisplayDataMapper: NotificationDisplayDataMapper,
    private val uiStateMachine: CustomNotificationUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CustomNotificationUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<CustomNotificationMessageState?>(null)
    val message: StateFlow<CustomNotificationMessageState?> = _message.asStateFlow()

    private val notificationsCache = mutableListOf<NotificationDisplayData>()
    private var currentPage = 1
    private var hasMoreData = true

    override fun onUiReady() {
        getCustomNotifications(forceRefresh = true)
    }

    fun onUiEvent(event: CustomNotificationUiEvent) {
        when (event) {
            is CustomNotificationUiEvent.LoadMore -> getCustomNotifications(forceRefresh = false)
            is CustomNotificationUiEvent.NotificationClicked -> onNotificationClicked(event.notification)
        }
    }

    fun onMessageHandled() = _message.update { null }

    private fun getCustomNotifications(forceRefresh: Boolean) {
        val userType = sharedScreenData.getCurrentUserType() ?: run {
            uiStateMachine.showError("User not found")
            return
        }

        if (forceRefresh) {
            notificationsCache.clear()
            currentPage = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)

            customNotificationRepository.getAll(
                userType = userType.type,
                page = currentPage,
                forceRefresh = forceRefresh,
            ).onSuccess { notifications ->
                if (notifications.isEmpty()) {
                    hasMoreData = false
                } else {
                    currentPage++
                }
                notificationsCache.addAll(notificationDisplayDataMapper.map(notifications))
                uiStateMachine.showNotifications(
                    groupedNotifications = notificationsCache.groupBy { it.formattedDate },
                )
            }.onFailure {
                uiStateMachine.showContentLoading(isLoading = false)
                val message = it.message ?: "Couldn't load notifications"
                if (notificationsCache.isEmpty()) {
                    uiStateMachine.showError(message)
                } else {
                    _message.update { CustomNotificationMessageState.ShowAlert(message = message) }
                }
            }
        }
    }

    private fun onNotificationClicked(notification: Notification.CustomNotification) {
        if (notification.readAt == 0L) {
            viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                customNotificationRepository.markCustomNotificationAsRead(notification)
                    .onSuccess { updatedNotification ->
                        updateNotificationInCache(updatedNotification)
                    }
            }
        }

        when (notification.category) {
            NotificationCategory.DEFAULT -> Unit
            NotificationCategory.NEW_FOLLOWER -> Unit
            NotificationCategory.NEW_FOLLOW_REQUEST -> Unit
            NotificationCategory.MESSAGE -> Unit
            NotificationCategory.BLOOD_DONATION -> Unit
            NotificationCategory.HELP -> Unit
        }
    }

    private fun updateNotificationInCache(updatedNotification: Notification.CustomNotification) {
        val index = notificationsCache.indexOfFirst { it.notification.id == updatedNotification.id }
        if (index < 0) return

        notificationsCache[index] = notificationDisplayDataMapper
            .map(listOf(updatedNotification))
            .first()

        uiStateMachine.showNotifications(
            groupedNotifications = notificationsCache.groupBy { it.formattedDate },
        )
    }
}
