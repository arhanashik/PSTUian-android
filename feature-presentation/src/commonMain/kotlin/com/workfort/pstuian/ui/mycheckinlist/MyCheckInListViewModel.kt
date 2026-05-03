package com.workfort.pstuian.ui.mycheckinlist

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
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiEvent
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInMessageState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInNavigationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyCheckInListViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val checkInRepo: CheckInRepository,
    private val uiStateMachine: MyCheckInListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<MyCheckInListUiState>(uiStateMachine) {

    private val _messageState = MutableStateFlow<MyCheckInMessageState?>(null)
    val message = _messageState.asStateFlow()

    private val _navigationState = MutableStateFlow<MyCheckInNavigationState?>(null)
    val navigation = _navigationState.asStateFlow()

    override fun onUiReady() {
        onUiEvent(MyCheckInListUiEvent.LoadMoreData(refresh = true))
    }

    fun onUiEvent(event: MyCheckInListUiEvent) {
        viewModelScope.launch {
            when (event) {
                is MyCheckInListUiEvent.LoadMoreData -> loadCheckInList(event.refresh)
                is MyCheckInListUiEvent.BackClicked -> {
                    _navigationState.update { MyCheckInNavigationState.GoBack }
                }
                is MyCheckInListUiEvent.ItemClicked -> onCheckInItemClicked(event.item)
                is MyCheckInListUiEvent.ChangePrivacy -> changePrivacy(event.item, event.privacy)
                is MyCheckInListUiEvent.Delete -> delete(event.item)
            }
        }
    }

    fun onMessageHandled() {
        _messageState.update { null }
    }

    fun onNavigationHandled() {
        _navigationState.update { null }
    }

    private fun isListLoading(): Boolean {
        return uiStateMachine.uiState.value.isLoading
    }

    private var page = 0
    private var endOfData: Boolean = false
    private val itemsCache = arrayListOf<CheckIn>()

    private suspend fun loadCheckInList(refresh: Boolean) {
        if (isListLoading() || (refresh.not() && endOfData)) {
            return
        }
        if (refresh) {
            page = 0
            endOfData = false
            itemsCache.clear()
        }
        page += 1
        uiStateMachine.updateLoading(true)

        checkInRepo.getHistory(userId, userType, page)
            .onSuccess { list ->
                if (list.isEmpty()) {
                    endOfData = true
                } else {
                    itemsCache.addAll(list)
                }
                uiStateMachine.updateData(itemsCache.toList())
            }
            .onFailure {
                endOfData = true
                if (itemsCache.isEmpty()) {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateError(message)
                } else {
                    uiStateMachine.updateLoading(false)
                }
            }
    }

    private fun onCheckInItemClicked(item: CheckIn) {
        _messageState.update {
            MyCheckInMessageState.ShowDetails(
                item = item,
                onClickChangePrivacy = { privacy -> changePrivacy(item, privacy) },
                onClickDelete = { delete(item) },
            )
        }
    }

    private fun changePrivacy(item: CheckIn, privacy: CheckInPrivacy) {
        uiStateMachine.updateOperationLoading(true)

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                checkInRepo.updatePrivacy(item.id, privacy.value)
            }.onSuccess {
                uiStateMachine.updateOperationLoading(false)
                _messageState.update { MyCheckInMessageState.Success("Changed successfully") }
                loadCheckInList(refresh = true)
            }.onFailure {
                uiStateMachine.updateOperationLoading(false)
                val message = it.message ?: "Failed to change. Please try again."
                _messageState.update { MyCheckInMessageState.Error(message) }
            }
        }
    }

    private fun delete(item: CheckIn) {
        uiStateMachine.updateOperationLoading(true)

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                checkInRepo.delete(item.id)
            }.onSuccess {
                uiStateMachine.updateOperationLoading(false)
                _messageState.update { MyCheckInMessageState.Success("Deleted successfully") }
                loadCheckInList(refresh = true)
            }.onFailure {
                uiStateMachine.updateOperationLoading(false)
                val message = it.message ?: "Failed to delete. Please try again"
                _messageState.update { MyCheckInMessageState.Error(message) }
            }
        }
    }
}
