package com.workfort.pstuian.app.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repo: NotificationRepository,
    private val stateReducer: NotificationScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<NotificationScreenState> get() = _screenState

    private fun updateScreenState(update: NotificationScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(NotificationScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(NotificationScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        NotificationScreenStateUpdate.NavigateTo(NotificationScreenState.NavigationState.GoBack)
    )

    fun onClickNotification(notification: NotificationEntity) = updateScreenState(
        NotificationScreenStateUpdate.UpdateMessageState(
            NotificationScreenState.DisplayState.MessageState.NotificationDetails(
                notification
            ),
        )
    )

    private var currentDataPage = 0
    private var hasMoreData = true
    private val notificationsCache = ArrayList<NotificationEntity>()

    private fun isNotificationLoading(): Boolean {
        val currentState = _screenState.value.displayState.notificationState
        return currentState is NotificationScreenState.DisplayState.NotificationState.Available
                && currentState.isLoading
    }

    fun getAll(isRefresh: Boolean = true) {
        if (isRefresh) {
            currentDataPage = 0
            notificationsCache.clear()
        }
        if (isNotificationLoading() || (isRefresh.not() && hasMoreData.not())) return
        viewModelScope.launch {
            updateScreenState(
                NotificationScreenStateUpdate.NotificationData(
                    notifications = notificationsCache,
                    isLoading = true,
                )
            )
            runCatching {
                currentDataPage += 1
                val notifications = repo.getAll(currentDataPage)
                notificationsCache.addAll(notifications)
                hasMoreData = true
                updateScreenState(
                    NotificationScreenStateUpdate.NotificationData(
                        notifications = notificationsCache,
                        isLoading = false,
                    )
                )
            }.onFailure {
                hasMoreData = false
                if (notificationsCache.isEmpty()) {
                    val error = it.message ?: "Failed to load notifications"
                    updateScreenState(NotificationScreenStateUpdate.NotificationLoadFailed(error))
                } else {
                    updateScreenState(
                        NotificationScreenStateUpdate.NotificationData(
                            notifications = notificationsCache,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }
}