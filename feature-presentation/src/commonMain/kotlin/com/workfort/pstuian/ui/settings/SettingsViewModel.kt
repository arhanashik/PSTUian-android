package com.workfort.pstuian.ui.settings

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.settings.state.SettingsMessageState
import com.workfort.pstuian.ui.settings.state.SettingsNavigationState
import com.workfort.pstuian.ui.settings.state.SettingsUiEvent
import com.workfort.pstuian.ui.settings.state.SettingsUiState
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.PushNotificationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val platformInfo: PlatformInfo,
    private val pushNotificationProvider: PushNotificationProvider,
    private val stateMachine: SettingsUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SettingsUiState>(stateMachine) {

    private val _message = MutableStateFlow<SettingsMessageState?>(null)
    val message: StateFlow<SettingsMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SettingsNavigationState?>(null)
    val navigation: StateFlow<SettingsNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        stateMachine.showInitialState(
            showNotification = settingsRepository.shouldShowNotification(),
            theme = settingsRepository.getTheme(),
            isDebug = platformInfo.isDebug,
            fcmToken = settingsRepository.getFcmToken() ?: "N/A",
            appUsageRole = settingsRepository.getAppUsageRole(),
            appVersionName = platformInfo.appVersionName,
            appVersionCode = platformInfo.appVersionCode,
            deviceId = platformInfo.deviceId,
        )
    }

    fun onUiEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.OnClickBack -> onClickBack()
                is SettingsUiEvent.OnClickContactUs -> onClickContactUs()
                is SettingsUiEvent.SetShowNotification -> setShowNotification(event.show)
                is SettingsUiEvent.OnChangeTheme -> onChangeTheme(event.theme)
                is SettingsUiEvent.OnRefreshFcmToken -> onRefreshFcmToken()
                is SettingsUiEvent.OnClearSharedPrefs -> onClearSharedPrefs()
                SettingsUiEvent.OnClickEditAppUsageRole -> openAppUsageRoleSelectionMessage()
            }
        }
    }

    private fun openAppUsageRoleSelectionMessage() {
        _message.update {
            SettingsMessageState.AppUsageRoleSelection(
                selectedRole = settingsRepository.getAppUsageRole(),
                onSelectRole = { role -> onAppUsageRoleSheetSelect(role) },
                onSaveAndContinue = { onAppUsageRoleSheetSave() },
            )
        }
    }

    private fun onAppUsageRoleSheetSelect(role: AppUsageRole) {
        _message.update { prev ->
            when (prev) {
                is SettingsMessageState.AppUsageRoleSelection -> prev.copy(selectedRole = role)
                else -> prev
            }
        }
    }

    private fun onAppUsageRoleSheetSave() {
        val sheet = _message.value as? SettingsMessageState.AppUsageRoleSelection ?: return
        val role = sheet.selectedRole ?: return
        settingsRepository.setAppUsageRole(role)
        stateMachine.setAppUsageRole(role)
        onMessageHandled()
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { SettingsNavigationState.GoBack }
    }

    private fun onClickContactUs() {
        _navigation.update { SettingsNavigationState.GoToContactUs }
    }

    private fun setShowNotification(show: Boolean) {
        settingsRepository.showNotification(show)
        stateMachine.setShowNotification(show)
    }

    private fun onChangeTheme(theme: ThemeMode) {
        settingsRepository.setTheme(theme)
        stateMachine.setTheme(theme)
    }

    private fun onRefreshFcmToken() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            val fcmToken = pushNotificationProvider.getPushToken()
            fcmToken?.let { settingsRepository.setFcmToken(it) }
            stateMachine.setFcmToken(fcmToken ?: "N/A")
        }
    }

    private fun onClearSharedPrefs() {
        _message.update {
            SettingsMessageState.ConfirmClearPrefs(
                title = "Clear Data",
                message = "Are you sure you want to clear all app data? This will log you out and reset all settings.",
                onConfirm = {
                    settingsRepository.clearSharedPrefs()
                    onUiReady()
                    onMessageHandled()
                }
            )
        }
    }
}
