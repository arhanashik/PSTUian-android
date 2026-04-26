package com.workfort.pstuian.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.usecase.GetSignedInUserUseCase
import com.workfort.pstuian.model.SharedScreenData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AppViewModel(
    private val sharedScreenData: SharedScreenData,
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val getSignedInUserUseCase: GetSignedInUserUseCase,
    private val userPresenceRepository: UserPresenceRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val appTheme: StateFlow<ThemeMode>
        get() = settingsRepository.observeTheme()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = settingsRepository.getTheme(),
            )

    init {
        observeSignedInUser()
    }

    private fun observeSignedInUser() {
        viewModelScope.launch (coroutineDispatcherProvider.io) {
            authRepository.observeSignedInAuthUser().collectLatest {
                val signInUser = getSignedInUserUseCase()
                sharedScreenData.setCurrentUser(signInUser)

                // update user status as online
                signInUser?.userId?.let { userId ->
                    userPresenceRepository.observeAndSyncUserPresence(userId)
                }
            }
        }
    }
}