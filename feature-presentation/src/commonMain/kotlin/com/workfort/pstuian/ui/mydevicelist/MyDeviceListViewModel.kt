package com.workfort.pstuian.ui.mydevicelist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListMessageState
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListNavigationState
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiEvent
import com.workfort.pstuian.ui.mydevicelist.state.MyDeviceListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyDeviceListViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: MyDeviceListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<MyDeviceListUiState>(stateMachine) {

    private val _message = MutableStateFlow<MyDeviceListMessageState?>(null)
    val message: StateFlow<MyDeviceListMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<MyDeviceListNavigationState?>(null)
    val navigation: StateFlow<MyDeviceListNavigationState?> = _navigation.asStateFlow()

    private var page = 0

    override fun onUiReady() {
        loadDeviceList(refresh = true)
    }

    fun onUiEvent(event: MyDeviceListUiEvent) {
        viewModelScope.launch {
            when (event) {
                is MyDeviceListUiEvent.RefreshClicked -> loadDeviceList(refresh = true)
                is MyDeviceListUiEvent.LoadMore -> loadDeviceList(refresh = false)
                is MyDeviceListUiEvent.BackClicked -> onClickBack()
                is MyDeviceListUiEvent.ItemClicked -> onClickItem(event.item)
                is MyDeviceListUiEvent.SignOutFromAllDeviceClicked -> onClickSignOutFromAllDevice()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { MyDeviceListNavigationState.GoBack }

    private fun onClickItem(item: DeviceEntity) {
        _message.update { MyDeviceListMessageState.ShowDetails(item) }
    }

    private fun onClickSignOutFromAllDevice() {
        if (stateMachine.uiState.value.devices.isEmpty()) {
            _message.update { MyDeviceListMessageState.Error("No device to sign out") }
        } else {
            _message.update {
                MyDeviceListMessageState.ConfirmSignOutFromAll {
                    signOutFromAllDevices()
                }
            }
        }
    }

    private fun loadDeviceList(refresh: Boolean) {
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

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                authRepo.getAllDevices(page)
            }.onSuccess { newList ->
                val allItems = currentItems + newList
                stateMachine.updateData(devices = allItems, isEndOfData = newList.isEmpty())
            }.onFailure {
                stateMachine.updateLoading(isLoading = false, isRefresh = refresh)
                val message = it.message ?: "Failed to load data"
                stateMachine.updateError(message = message, isFirstPage = currentItems.isEmpty())
            }
        }
    }

    private fun signOutFromAllDevices() {
        _message.update { MyDeviceListMessageState.Loading(cancelable = false) }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                authRepo.signOut(fromAllDevice = true)
            }.onSuccess {
                _message.update { null }
                _navigation.update { MyDeviceListNavigationState.GoBack }
            }.onFailure {
                val message = it.message ?: "Failed to sign out from all devices. Please try again."
                _message.update { MyDeviceListMessageState.Error(message) }
            }
        }
    }
}
