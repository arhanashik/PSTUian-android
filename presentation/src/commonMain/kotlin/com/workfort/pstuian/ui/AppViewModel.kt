package com.workfort.pstuian.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.model.SystemNotification
import com.workfort.pstuian.featuredomain.model.SystemNotificationDisplayType
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.getUserPresenceId
import com.workfort.pstuian.featuredomain.repository.AppConfigRepository
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.SystemNotificationRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import com.workfort.pstuian.featuredomain.usecase.GetSignedInUserUseCase
import com.workfort.pstuian.model.AppLaunchDeepLinkController
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.navigation.AppNavigator
import com.workfort.pstuian.ui.common.navigation.DeepLinkNavigator
import com.workfort.pstuian.util.deeplink.DeepLinkAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val sharedScreenData: SharedScreenData,
    private val appConfigRepository: AppConfigRepository,
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val systemNotificationRepository: SystemNotificationRepository,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val userPresenceRepository: UserPresenceRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val appLaunchDeepLinkController: AppLaunchDeepLinkController,
    private val appNavigator: AppNavigator,
    private val deepLinkNavigator: DeepLinkNavigator,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode>
        get() = settingsRepository.observeTheme()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = settingsRepository.getTheme(),
            )

    val newSystemNotificationForAlert = MutableStateFlow<SystemNotification?>(null)

    init {
        observeAppConfig()
        observeSignedInUser()
        observeNewSystemNotification()
    }

    fun onLaunchDeepLink(savedInstanceRestored: Boolean, deepLinkAction: DeepLinkAction?) {
        if (savedInstanceRestored) return
        if (deepLinkAction == null) return
        appLaunchDeepLinkController.setPendingDeepLink(deepLinkAction)
    }

    fun onNewIntentDeepLink(deepLinkAction: DeepLinkAction?) {
        if (deepLinkAction == null) return
        viewModelScope.launch {
            deepLinkNavigator.navigate(deepLinkAction, appNavigator)
        }
    }

    private fun observeAppConfig() {
        viewModelScope.launch (coroutineDispatcherProvider.io) {
            appConfigRepository.observeAppConfig().collectLatest {
                sharedScreenData.setAppConfig(it)
            }
        }
    }

    private fun observeSignedInUser() {
        viewModelScope.launch (coroutineDispatcherProvider.io) {
            authRepository.observeSignedInAuthUser().collectLatest {
                val signInUser = getSignedInUserUseCase()
                sharedScreenData.setCurrentUser(signInUser)

                // update user status as online
                signInUser?.getUserPresenceId()?.let { presenceId ->
                    userPresenceRepository.observeAndSyncUserPresence(presenceId)
                }
            }
        }
    }

    private fun observeNewSystemNotification() {
        systemNotificationRepository.observeNewSystemNotification(SystemNotificationDisplayType.ALERT)
            .onEach { notification ->
                if (notification.requireSignIn) {
                    if (authRepository.isUserSignedIn()) {
                        newSystemNotificationForAlert.value = notification
                    }
                } else {
                    newSystemNotificationForAlert.value = notification
                }
            }
            .flowOn(coroutineDispatcherProvider.io)
            .launchIn(viewModelScope)
    }

    fun dismissNotificationAlert(dontShowAgain: Boolean) {
        if (dontShowAgain) {
            systemNotificationRepository.updateSystemNotificationClosedTimestamp(
                type = SystemNotificationDisplayType.ALERT,
            )
        }
        newSystemNotificationForAlert.value = null
    }
}