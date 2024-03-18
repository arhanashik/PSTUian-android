package com.workfort.pstuian.app.ui.notification.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.notification.intent.NotificationIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.NotificationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
 *  ****************************************************************************
 */
class NotificationViewModel(private val repo: NotificationRepository) : ViewModel() {
    val intent = Channel<NotificationIntent>(Channel.UNLIMITED)

    var notificationsPage = 1
    private val _notificationsState = MutableStateFlow<RequestState>(RequestState.Idle)
    val notificationsState: StateFlow<RequestState> get() = _notificationsState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when(it) {
                    is NotificationIntent.GetAll -> getAll(it.page)
                }
            }
        }
    }

    private fun getAll(page: Int) {
        viewModelScope.launch {
            _notificationsState.value = RequestState.Loading
            _notificationsState.value = try {
                RequestState.Success(repo.getAll(page))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}