package com.workfort.pstuian.app.ui.settings

import com.workfort.pstuian.view.service.StateUpdate

sealed interface SettingsScreenStateUpdate : StateUpdate<SettingsScreenState> {

    data class ShowNotification(val show: Boolean) : SettingsScreenStateUpdate {
        override fun invoke(
            oldState: SettingsScreenState,
        ): SettingsScreenState = with(oldState) {
            copy(displayState = displayState.copy(showNotification = show))
        }
    }

    data class UpdateMessageState(
        val messageState: SettingsScreenState.DisplayState.MessageState,
    ) : SettingsScreenStateUpdate {
        override fun invoke(
            oldState: SettingsScreenState
        ): SettingsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : SettingsScreenStateUpdate {
        override fun invoke(
            oldState: SettingsScreenState
        ): SettingsScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: SettingsScreenState.NavigationState,
    ) : SettingsScreenStateUpdate {
        override fun invoke(
            oldState: SettingsScreenState,
        ): SettingsScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : SettingsScreenStateUpdate {
        override fun invoke(
            oldState: SettingsScreenState,
        ): SettingsScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}