package com.workfort.pstuian.ui.notification

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Notification
import com.workfort.pstuian.featuredomain.model.NotificationCategory
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.getPrefixUserId
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CustomNotificationRepository
import com.workfort.pstuian.featuredomain.repository.SystemNotificationRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.notification.displaydata.NotificationDisplayData
import com.workfort.pstuian.ui.notification.state.NotificationMessageState
import com.workfort.pstuian.ui.notification.state.NotificationNavigationState
import com.workfort.pstuian.ui.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.state.NotificationUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

internal class NotificationViewModel(
    private val systemNotificationRepository: SystemNotificationRepository,
    private val customNotificationRepository: CustomNotificationRepository,
    private val sharedScreenData: SharedScreenData,
    private val notificationDisplayDataMapper: NotificationDisplayDataMapper,
    private val uiStateMachine: NotificationUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<NotificationUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<NotificationMessageState?>(null)
    val message: StateFlow<NotificationMessageState?> = _message

    private val _navigation = MutableStateFlow<NotificationNavigationState?>(null)
    val navigation: StateFlow<NotificationNavigationState?> = _navigation

    private val _systemNotifications = mutableListOf<NotificationDisplayData>()
    private val _customNotifications = mutableListOf<NotificationDisplayData>()

    private val currentUser: User? by lazy { sharedScreenData.getCurrentUser() }
    private var systemObservationJob: Job? = null

    override fun onUiReady() {
        uiStateMachine.showLoading(true)
        observeSystemNotifications()
        getCustomNotifications()
    }

    fun onEvent(event: NotificationUiEvent) {
        when (event) {
            is NotificationUiEvent.BackClicked -> _navigation.update { NotificationNavigationState.GoBack }
            is NotificationUiEvent.TabSelected -> onTabSelected(event.tabIndex)
            is NotificationUiEvent.NotificationClicked -> onNotificationClicked(event.notification)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onTabSelected(tabIndex: Int) {
        uiStateMachine.selectTab(tabIndex)
    }

    private fun observeSystemNotifications() {
        val userId = currentUser?.getPrefixUserId() ?: return
        systemObservationJob?.cancel()

        systemObservationJob = systemNotificationRepository.observeSystemNotifications(userId)
            .onEach { notifications ->
                _systemNotifications.clear()
                _systemNotifications.addAll(notificationDisplayDataMapper.map(notifications))
                uiStateMachine.updateSystemNotifications(
                    groupedNotifications = _systemNotifications.groupBy { it.formattedDate },
                )
            }
            .flowOn(coroutineDispatcherProvider.main)
            .launchIn(viewModelScope)
    }

    private fun getCustomNotifications() {
        val userId = currentUser?.userId ?: return

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            customNotificationRepository.getCustomNotifications(userId).onSuccess { notifications ->
                val notifications = notificationDisplayDataMapper.map(notifications)
                _customNotifications.clear()
                _customNotifications.addAll(notifications)
                uiStateMachine.updateCustomNotifications(
                    groupedNotifications = _customNotifications.groupBy { it.formattedDate },
                )
            }.onFailure {
                val message = it.message ?: "Couldn't load notifications"
                _message.update { NotificationMessageState.ShowAlert(message = message) }
            }
        }
    }

    private fun onNotificationClicked(notification: Notification) {
        when (notification) {
            is Notification.SystemNotification -> {
                _message.update { NotificationMessageState.ShowSystemNotification(notification) }

                val userId = currentUser?.getPrefixUserId() ?: return
                if (notification.readAt == 0L) { // unread message
                    viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                        systemNotificationRepository.markSystemNotificationAsRead(userId, notification.id)
                            .onSuccess { observeSystemNotifications() }
                    }
                }
            }
            is Notification.CustomNotification -> onCustomNotificationClicked(notification)
        }
    }

    private fun onCustomNotificationClicked(notification: Notification.CustomNotification) {
        val userId = currentUser?.userId ?: return
        if (notification.readAt == 0L) { // unread message
            viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                customNotificationRepository.markCustomNotificationAsRead(userId, notification.id)
            }
        }

        when (notification.category) {
            NotificationCategory.DEFAULT -> { }
            NotificationCategory.NEW_FOLLOWER -> { }
            NotificationCategory.NEW_FOLLOW_REQUEST -> { }
            NotificationCategory.MESSAGE -> { }
            NotificationCategory.BLOOD_DONATION -> { }
            NotificationCategory.HELP -> { }
        }
    }
}
