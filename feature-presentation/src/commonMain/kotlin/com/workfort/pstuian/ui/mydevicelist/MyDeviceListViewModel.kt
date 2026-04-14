package com.workfort.pstuian.ui.mydevicelist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiEvent
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import kotlinx.coroutines.launch

class MyDeviceListViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: MyDeviceListUiStateMachine,
) : UiStateMachineViewModel<MyDeviceListUiState>(stateMachine) {

    private var page = 0

    override fun onUiReady() {
        onUiEvent(MyDeviceListUiEvent.OnLoadList(refresh = true))
    }

    fun onUiEvent(event: MyDeviceListUiEvent) {
        viewModelScope.launch {
            when (event) {
                is MyDeviceListUiEvent.OnClickBack -> stateMachine.onClickBack()
                is MyDeviceListUiEvent.OnClickItem -> stateMachine.onClickItem(event.item)
                is MyDeviceListUiEvent.OnClickSignOutFromAllDevice -> {
                    stateMachine.onClickSignOutFromAllDevice(stateMachine.uiState.value.devices.isNotEmpty())
                }
                is MyDeviceListUiEvent.OnConfirmSignOutFromAll -> signOutFromAllDevices()
                is MyDeviceListUiEvent.OnLoadList -> loadDeviceList(event.refresh)
                is MyDeviceListUiEvent.MessageConsumed -> stateMachine.messageConsumed()
                is MyDeviceListUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
            }
        }
    }

    private suspend fun loadDeviceList(refresh: Boolean) {
        val currentState = stateMachine.uiState.value
        if (currentState.isLoading || (refresh.not() && currentState.isEndOfData)) {
            return
        }

        if (refresh) {
            page = 0
        }

        page += 1

        val currentItems = if (refresh) emptyList() else currentState.devices
        stateMachine.updateLoading(isLoading = true, isRefresh = refresh)

        runCatching {
            authRepo.getAllDevices(page)
        }.onSuccess { newList ->
            val allItems = currentItems + newList
            stateMachine.updateData(devices = allItems, isEndOfData = newList.isEmpty())
        }.onFailure {
            val message = it.message ?: "Failed to load data"
            stateMachine.updateError(message = message, isFirstPage = currentItems.isEmpty())
        }
    }

    private suspend fun signOutFromAllDevices() {
        stateMachine.updateMessageState(MyDeviceListUiState.MessageState.Loading(cancelable = false))

        runCatching {
            authRepo.signOut(fromAllDevice = true)
        }.onSuccess {
            stateMachine.onSignedOutFromAll()
        }.onFailure {
            val message = it.message ?: "Failed to sign out from all devices. Please try again."
            stateMachine.updateMessageState(MyDeviceListUiState.MessageState.Error(message))
        }
    }
}
