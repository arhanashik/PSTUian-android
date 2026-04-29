package com.workfort.pstuian.ui.mydevicelist

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.DeviceRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.model.SharedScreenData
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
    private val screenData: SharedScreenData,
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository,
    private val settingsRepository: SettingsRepository,
    private val stateMachine: MyDeviceListUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<MyDeviceListUiState>(stateMachine) {

    private val _message = MutableStateFlow<MyDeviceListMessageState?>(null)
    val message: StateFlow<MyDeviceListMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<MyDeviceListNavigationState?>(null)
    val navigation: StateFlow<MyDeviceListNavigationState?> = _navigation.asStateFlow()

    private var page = 0

    override fun onUiReady() {
        loadDeviceList(isRefresh = true)
    }

    fun onUiEvent(event: MyDeviceListUiEvent) {
        viewModelScope.launch {
            when (event) {
                is MyDeviceListUiEvent.RefreshClicked -> loadDeviceList(isRefresh = true)
                is MyDeviceListUiEvent.LoadMore -> loadDeviceList(isRefresh = false)
                is MyDeviceListUiEvent.BackClicked -> onClickBack()
                is MyDeviceListUiEvent.ItemClicked -> onClickItem(event.item)
                is MyDeviceListUiEvent.SignOutFromAllDeviceClicked -> onClickSignOutFromAllDevice()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { MyDeviceListNavigationState.GoBack }

    private fun onClickItem(item: Device) {
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

    private fun loadDeviceList(isRefresh: Boolean) {
        val userId = screenData.getCurrentUser()?.authUserId ?: return
        val userType = screenData.getCurrentUserType() ?: return

        val currentState = stateMachine.uiState.value
        if (currentState.isLoading || (isRefresh.not() && currentState.isEndOfData)) {
            return
        }

        if (isRefresh) {
            page = 0
            deviceRepository.clearCache()
        }
        page += 1

        val currentItems = if (isRefresh) emptyList() else currentState.devices
        stateMachine.showLoading(isLoading = true)

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            deviceRepository.getAllDevices(userId, userType, page)
                .onSuccess {
                    stateMachine.updateDevices(devices = it, isRefresh = isRefresh)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    stateMachine.updateError(message = message, isFirstPage = currentItems.isEmpty())
                }
        }
    }

    private fun signOutFromAllDevices() {
        val userType = settingsRepository.getUserType() ?: return

        _message.update { MyDeviceListMessageState.Loading(cancelable = false) }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                authRepository.signOut(userType, fromAllDevice = true)
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
