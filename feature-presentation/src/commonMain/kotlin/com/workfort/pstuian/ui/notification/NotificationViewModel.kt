package com.workfort.pstuian.ui.notification

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.model.NotificationEntity
import com.workfort.pstuian.featuredomain.repository.NotificationRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.notification.state.NotificationMessageState
import com.workfort.pstuian.ui.notification.state.NotificationNavigationState
import com.workfort.pstuian.ui.notification.state.NotificationUiEvent
import com.workfort.pstuian.ui.notification.state.NotificationUiState
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repo: NotificationRepository,
    private val sharedScreenData: SharedScreenData,
    private val stateMachine: NotificationUiStateMachine,
) : UiStateMachineViewModel<NotificationUiState>(stateMachine) {

    override fun onUiReady() {
        getAll(isRefresh = true)
    }

    fun onEvent(event: NotificationUiEvent) {
        when (event) {
            is NotificationUiEvent.OnClickBack -> onClickBack()
            is NotificationUiEvent.OnClickNotification -> onClickNotification(event.notification)
            is NotificationUiEvent.OnLoadMore -> getAll(isRefresh = false)
            is NotificationUiEvent.OnRefresh -> getAll(isRefresh = true)
            is NotificationUiEvent.MessageConsumed -> messageConsumed()
            is NotificationUiEvent.NavigationConsumed -> navigationConsumed()
        }
    }

    private fun onClickBack() {
        stateMachine.navigateTo(NotificationNavigationState.GoBack)
    }

    private fun onClickNotification(notification: NotificationEntity) {
        stateMachine.showMessage(NotificationMessageState.NotificationDetails(notification))
    }

    private var currentDataPage = 0
    private var hasMoreData = true
    private val notificationsCache = ArrayList<NotificationEntity>()

    private fun getAll(isRefresh: Boolean = true) {
        val userId = sharedScreenData.getCurrentUser()?.userId ?: return
        val userType = sharedScreenData.getCurrentUserType() ?: return

        if (isRefresh) {
            currentDataPage = 0
            notificationsCache.clear()
            hasMoreData = true
        }
        if (uiState.value.isLoading || !hasMoreData) return

        viewModelScope.launch {
            stateMachine.updateNotifications(
                notifications = notificationsCache,
                isLoading = true,
            )
            runCatching {
                currentDataPage += 1
                val notifications = repo.getAll(userId, userType, currentDataPage)
                if (notifications.isEmpty()) {
                    hasMoreData = false
                } else {
                    notificationsCache.addAll(notifications)
                }
                stateMachine.updateNotifications(
                    notifications = notificationsCache,
                    isLoading = false,
                )
            }.onFailure {
                hasMoreData = false
                if (notificationsCache.isEmpty()) {
                    val error = it.message ?: "Failed to load notifications"
                    stateMachine.updateError(error)
                } else {
                    stateMachine.updateNotifications(
                        notifications = notificationsCache,
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun messageConsumed() {
        stateMachine.showMessage(null)
    }

    fun navigationConsumed() {
        stateMachine.navigateTo(null)
    }
}
