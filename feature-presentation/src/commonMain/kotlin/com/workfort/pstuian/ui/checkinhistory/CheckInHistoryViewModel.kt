package com.workfort.pstuian.ui.checkinhistory

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiEvent
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiState
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryMessageState
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryNavigationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CheckInHistoryViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val checkInRepo: CheckInRepository,
    private val uiStateMachine: CheckInHistoryUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<CheckInHistoryUiState>(uiStateMachine) {

    private val _messageState = MutableStateFlow<CheckInHistoryMessageState?>(null)
    val message = _messageState.asStateFlow()

    private val _navigationState = MutableStateFlow<CheckInHistoryNavigationState?>(null)
    val navigation = _navigationState.asStateFlow()

    private var page = 1
    private var hasMoreData: Boolean = true
    private val checkInsCache = mutableListOf<CheckIn>()

    override fun onUiReady() {
        loadCheckInList(forceRefresh = false)
    }

    fun onUiEvent(event: CheckInHistoryUiEvent) {
        when (event) {
            is CheckInHistoryUiEvent.BackClicked -> _navigationState.update { CheckInHistoryNavigationState.GoBack }
            is CheckInHistoryUiEvent.LoadMore -> loadCheckInList(forceRefresh = false)
            is CheckInHistoryUiEvent.ItemClicked -> onCheckInItemClicked(event.item)
            is CheckInHistoryUiEvent.ChangePrivacy -> changePrivacy(event.item, event.privacy)
            is CheckInHistoryUiEvent.Delete -> delete(event.item)
        }
    }

    fun onMessageHandled() {
        _messageState.update { null }
    }

    fun onNavigationHandled() {
        _navigationState.update { null }
    }

    private fun loadCheckInList(forceRefresh: Boolean) {
        if (forceRefresh) {
            checkInsCache.clear()
            page = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        uiStateMachine.updateContentLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            checkInRepo.getHistory(userId, userType, page, forceRefresh)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        hasMoreData = false
                    } else {
                        page++
                    }
                    checkInsCache.addAll(list)
                    uiStateMachine.showCheckIns(checkInsCache)
                }
                .onFailure {
                    uiStateMachine.updateContentLoading(false)
                    if (checkInsCache.isEmpty()) {
                        val message = it.message ?: "Failed to load data"
                        uiStateMachine.showError(message)
                    }
                }
        }
    }

    private fun onCheckInItemClicked(item: CheckIn) {
        _messageState.update {
            CheckInHistoryMessageState.ShowDetails(
                item = item,
                onClickChangePrivacy = { privacy -> changePrivacy(item, privacy) },
                onClickDelete = { delete(item) },
            )
        }
    }

    private fun changePrivacy(item: CheckIn, privacy: CheckInPrivacy) {
        _messageState.update {
            CheckInHistoryMessageState.ConfirmPrivacyChange(item, privacy) {
                uiStateMachine.updateOperationLoading(true)
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    checkInRepo.updatePrivacy(item.id, privacy.value).onSuccess {
                        uiStateMachine.updateOperationLoading(false)
                        _messageState.update { CheckInHistoryMessageState.Success("Changed successfully") }
                        loadCheckInList(forceRefresh = true)
                    }.onFailure {
                        uiStateMachine.updateOperationLoading(false)
                        val message = it.message ?: "Failed to change. Please try again."
                        _messageState.update { CheckInHistoryMessageState.Error(message) }
                    }
                }
            }
        }
    }

    private fun delete(item: CheckIn) {
        _messageState.update {
            CheckInHistoryMessageState.ConfirmDelete(item) {
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    uiStateMachine.updateOperationLoading(true)
                    checkInRepo.delete(item.id).onSuccess {
                        uiStateMachine.updateOperationLoading(false)
                        _messageState.update { CheckInHistoryMessageState.Success("Deleted successfully") }
                        loadCheckInList(forceRefresh = true)
                    }.onFailure {
                        uiStateMachine.updateOperationLoading(false)
                        val message = it.message ?: "Failed to delete. Please try again"
                        _messageState.update { CheckInHistoryMessageState.Error(message) }
                    }
                }
            }
        }
    }
}
