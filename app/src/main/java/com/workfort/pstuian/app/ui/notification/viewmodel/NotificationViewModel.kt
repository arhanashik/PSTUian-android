package com.workfort.pstuian.app.ui.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.NotificationRepository
import com.workfort.pstuian.app.ui.notification.intent.NotificationIntent
import com.workfort.pstuian.app.ui.notification.viewstate.NotificationsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:02.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */
class NotificationViewModel(
    private val repo: NotificationRepository
) : ViewModel() {
    val intent = Channel<NotificationIntent>(Channel.UNLIMITED)

    private val _notificationsState = MutableStateFlow<NotificationsState>(NotificationsState.Idle)
    val notificationsState: StateFlow<NotificationsState> get() = _notificationsState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when(it) {
                    NotificationIntent.GetAll -> getAll()
                }
            }
        }
    }

    private fun getAll() {
        viewModelScope.launch {
            _notificationsState.value = NotificationsState.Loading
            _notificationsState.value = try {
                NotificationsState.Notifications(repo.getAll())
            } catch (e: Exception) {
                NotificationsState.Error(e.message)
            }
        }
    }
}