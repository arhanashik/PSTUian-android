package com.workfort.pstuian.app.ui.splash

import com.workfort.pstuian.view.service.StateUpdate


sealed interface SplashScreenStateUpdate : StateUpdate<SplashScreenState> {

    data class UpdateLoadingText(val text: String) : SplashScreenStateUpdate {
        override fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(displayState = displayState.copy(loadingText = text))
        }
    }

    data class UpdateMessageState(
        val messageState: SplashScreenState.DisplayState.MessageState
    ) : SplashScreenStateUpdate {
        override fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object GoToHomeScreen : SplashScreenStateUpdate {
        override fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(navigationState = SplashScreenState.NavigationState.HomeScreen)
        }
    }

    data object MessageConsumed : SplashScreenStateUpdate {
        override fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data object NavigationConsumed : SplashScreenStateUpdate {
        override fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}