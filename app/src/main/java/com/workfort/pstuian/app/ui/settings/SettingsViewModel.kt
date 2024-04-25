package com.workfort.pstuian.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val repo: SettingsRepository,
    private val reducer: SettingsScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<SettingsScreenState> get() = _screenState

    private fun updateScreenState(update: SettingsScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(SettingsScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(SettingsScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        SettingsScreenStateUpdate.NavigateTo(
            SettingsScreenState.NavigationState.GoBack,
        ),
    )

    fun loadInitialData() {
        val showNotification = repo.shouldShowNotification()
        updateScreenState(
            SettingsScreenStateUpdate.ShowNotification(showNotification)
        )
    }

    fun setShowNotification(show: Boolean) {
        viewModelScope.launch {
            runCatching {
                repo.setShowNotification(show)
            }.onSuccess {
                updateScreenState(
                    SettingsScreenStateUpdate.ShowNotification(show),
                )
            }.onFailure {
                val message = it.message ?: "Failed to change the settings"
                updateScreenState(
                    SettingsScreenStateUpdate.UpdateMessageState(
                        SettingsScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}