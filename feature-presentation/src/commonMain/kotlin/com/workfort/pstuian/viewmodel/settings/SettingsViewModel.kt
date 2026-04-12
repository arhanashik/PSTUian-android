package com.workfort.pstuian.app.ui.commonmodel.settings

import com.workfort.pstuian.repository.SettingsRepository
import com.workfort.pstuian.reducer.ui.settings.SettingsScreenState
import com.workfort.pstuian.reducer.ui.settings.SettingsScreenStateReducer
import com.workfort.pstuian.reducer.ui.settings.SettingsScreenStateUpdate
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val repo: SettingsRepository,
    private val reducer: SettingsScreenStateReducer,
) : BaseViewModel() {

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

    fun loadInitial() {
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