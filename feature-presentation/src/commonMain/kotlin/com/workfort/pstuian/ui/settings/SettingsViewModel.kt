package com.workfort.pstuian.ui.settings

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.settings.state.DebugPanelData
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
    private val authRepository: AuthRepository,
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
        val debugPanelData = if (platformInfo.isDebug) {
            DebugPanelData(
                fcmToken = settingsRepository.getFcmToken() ?: "N/A",
                debugApiEnvironment = settingsRepository.getDebugApiEnvironment(),
            )
        } else null

        stateMachine.showInitialState(
            userType = settingsRepository.getUserType(),
            theme = settingsRepository.getTheme(),
            showNotification = settingsRepository.shouldShowNotification(),
            appVersionName = platformInfo.appVersionName,
            appVersionCode = platformInfo.appVersionCode,
            deviceId = platformInfo.deviceId,
            debugPanelData = debugPanelData,
        )
    }

    fun onUiEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.BackClicked -> _navigation.update { SettingsNavigationState.GoBack }
                SettingsUiEvent.UserTypeClicked -> openAppUsageRoleSelectionMessage()
                SettingsUiEvent.ThemeClicked -> Unit
                is SettingsUiEvent.ShowNotificationToggled -> setShowNotification(event.show)
                is SettingsUiEvent.ChangeThemeClicked -> onChangeTheme(event.theme)
                is SettingsUiEvent.RefreshFcmTokenClicked -> onRefreshFcmToken()
                is SettingsUiEvent.ClearCacheClicked -> onClearCache()
                is SettingsUiEvent.ForceSignOutClicked -> onForceSignOut()
                SettingsUiEvent.DebugApiServerClicked -> Unit
                is SettingsUiEvent.DebugApiEnvironmentSelected -> {
                    settingsRepository.setDebugApiEnvironment(event.environment)
                    stateMachine.setDebugApiEnvironment(event.environment)
                }
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun openAppUsageRoleSelectionMessage() {
        _message.update {
            SettingsMessageState.UserTypeSelection(selectedUserType = settingsRepository.getUserType()) { userType ->
                settingsRepository.setUserType(userType)
                stateMachine.setUserType(userType)
                onMessageHandled()
            }
        }
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

    private fun onClearCache() {
        val userType = settingsRepository.getUserType() ?: return
        _message.update {
            SettingsMessageState.ConfirmAction(
                title = "Clear Data",
                message = "Are you sure you want to clear all app data? This will log you out and reset all settings.",
            ) {
                onMessageHandled()
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    authRepository.signOut(userType, fromAllDevice = false)
                    settingsRepository.clearSharedPrefs()
                    _navigation.update { SettingsNavigationState.ResetToRoot }
                }
            }
        }
    }

    private fun onForceSignOut() {
        val userType = settingsRepository.getUserType() ?: return
        _message.update {
            SettingsMessageState.ConfirmAction(
                title = "Force sign out",
                message = "Are you sure you want to force sign out?",
            ) {
                onMessageHandled()
                viewModelScope.launchOnMain(coroutineDispatcherProvider) {
                    authRepository.signOut(userType, fromAllDevice = false)
                        .onSuccess { _navigation.update { SettingsNavigationState.ResetToRoot } }
                        .onFailure { err ->
                            _message.update {
                                SettingsMessageState.Error(err.message ?: "Sign out failed. Please try again.")
                            }
                        }
                }
            }
        }
    }
}
