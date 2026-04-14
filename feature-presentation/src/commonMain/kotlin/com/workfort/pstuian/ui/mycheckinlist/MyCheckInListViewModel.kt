package com.workfort.pstuian.ui.mycheckinlist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.InitializationMode
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.CheckInPrivacy
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.ui.mycheckinlist.state.MessageState
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import com.workfort.pstuian.ui.mycheckinlist.state.NavigationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MyCheckInListViewModel(
    private val userId: Int,
    private val userType: UserType,
    private val checkInRepo: CheckInRepository,
    private val uiStateMachine: MyCheckInListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<MyCheckInListUiState>(uiStateMachine) {

    private val _messageState = MutableStateFlow<MessageState?>(null)
    val messageState: StateFlow<MessageState?> = _messageState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    override fun onUiReady() {
        loadCheckInList(refresh = true)
    }

    fun messageConsumed() {
        _messageState.update { null }
    }

    fun navigationConsumed() {
        _navigationState.update { null }
    }

    fun onClickBack() {
        _navigationState.update { NavigationState.GoBack }
    }

    fun onClickItem(item: CheckInEntity) {
        _messageState.update { MessageState.ShowDetails(item) }
    }

    fun onClickChangePrivacy(item: CheckInEntity, privacy: CheckInPrivacy) {
        _messageState.update { MessageState.ConfirmPrivacyChange(item, privacy) }
    }

    fun onClickDelete(item: CheckInEntity) {
        _messageState.update { MessageState.ConfirmDelete(item) }
    }

    private fun isListLoading(): Boolean {
        val state = uiStateMachine.uiState.value
        return if (state is MyCheckInListUiState.Content) {
            state.isLoading
        } else {
            false
        }
    }

    private var page = 0
    private var endOfData: Boolean = false
    private val itemsCache = arrayListOf<CheckInEntity>()

    fun loadCheckInList(refresh: Boolean) {
        if (isListLoading() || (refresh.not() && endOfData)) {
            return
        }
        if (refresh) {
            page = 0
            endOfData = false
            itemsCache.clear()
        }
        page += 1
        uiStateMachine.showLoading(true)
        if (itemsCache.isNotEmpty()) {
            uiStateMachine.showContent(itemsCache.toList())
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                val list = checkInRepo.getAll(
                    userId = userId,
                    userType = userType.type,
                    page = page,
                )
                if (list.isEmpty()) {
                    endOfData = true
                } else {
                    itemsCache.addAll(list)
                }
                uiStateMachine.showContent(itemsCache.toList())
            }.onFailure {
                endOfData = true
                if (itemsCache.isEmpty()) {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.showError(message)
                } else {
                    uiStateMachine.showLoading(false)
                }
            }
        }
    }

    fun changePrivacy(item: CheckInEntity, privacy: CheckInPrivacy) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showOperationLoading(true)
            runCatching {
                checkInRepo.updatePrivacy(item.id, privacy.value)
            }.onSuccess {
                uiStateMachine.showOperationLoading(false)
                _messageState.update { MessageState.Success("Changed successfully") }
                loadCheckInList(refresh = true)
            }.onFailure {
                uiStateMachine.showOperationLoading(false)
                val message = it.message ?: "Failed to change. Please try again."
                _messageState.update { MessageState.Error(message) }
            }
        }
    }

    fun delete(item: CheckInEntity) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showOperationLoading(true)
            runCatching {
                checkInRepo.delete(item.id)
            }.onSuccess {
                uiStateMachine.showOperationLoading(false)
                _messageState.update { MessageState.Success("Deleted successfully") }
                loadCheckInList(refresh = true)
            }.onFailure {
                uiStateMachine.showOperationLoading(false)
                val message = it.message ?: "Failed to delete. Please try again"
                _messageState.update { MessageState.Error(message) }
            }
        }
    }
}
