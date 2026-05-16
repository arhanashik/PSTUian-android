package com.workfort.pstuian.ui.notification.systemnotification

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SystemNotificationRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.notification.common.NotificationDisplayDataMapper
import com.workfort.pstuian.ui.notification.common.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationMessageState
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiEvent
import com.workfort.pstuian.ui.notification.systemnotification.state.SystemNotificationUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SystemNotificationViewModel(
    private val authRepository: AuthRepository,
    private val systemNotificationRepository: SystemNotificationRepository,
    private val notificationDisplayDataMapper: NotificationDisplayDataMapper,
    private val uiStateMachine: SystemNotificationUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SystemNotificationUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<SystemNotificationMessageState?>(null)
    val message: StateFlow<SystemNotificationMessageState?> = _message.asStateFlow()

    private val notificationsCache = mutableListOf<NotificationDisplayData>()
    private var observationJob: Job? = null

    override fun onUiReady() {
        uiStateMachine.showLoading()
        observeSystemNotifications()
    }

    fun onUiEvent(event: SystemNotificationUiEvent) {
        when (event) {
            is SystemNotificationUiEvent.NotificationClicked -> onNotificationClicked(event.notification)
        }
    }

    fun onMessageHandled() = _message.update { null }

    private fun observeSystemNotifications() {
        val authUserId = authRepository.getAuthUser()?.userId ?: run {
            uiStateMachine.showError("User not found")
            return
        }

        observationJob?.cancel()
        observationJob = systemNotificationRepository.observeSystemNotifications(authUserId)
            .onEach { notifications ->
                notificationsCache.clear()
                notificationsCache.addAll(notificationDisplayDataMapper.map(notifications))
                uiStateMachine.showNotifications(
                    groupedNotifications = notificationsCache.groupBy { it.formattedDate },
                )
            }
            .flowOn(coroutineDispatcherProvider.main)
            .launchIn(viewModelScope)
    }

    private fun onNotificationClicked(notification: Notification.SystemNotification) {
        _message.update { SystemNotificationMessageState.ShowDetail(notification) }

        val authUserId = authRepository.getAuthUser()?.userId ?: run {
            uiStateMachine.showError("User not found")
            return
        }
        if (notification.readAt == 0L) {
            viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                systemNotificationRepository.markSystemNotificationAsRead(authUserId, notification.id)
                    .onSuccess { observeSystemNotifications() }
            }
        }
    }
}
