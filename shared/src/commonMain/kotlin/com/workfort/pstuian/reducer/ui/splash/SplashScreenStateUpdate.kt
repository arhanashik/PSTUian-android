package com.workfort.pstuian.reducer.ui.splash

import com.workfort.pstuian.reducer.service.StateUpdate


sealed interface SplashScreenStateUpdate : StateUpdate<SplashScreenState> {

    data class UpdateLoadingText(val text: String) : SplashScreenStateUpdate {
        override operator fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(displayState = displayState.copy(loadingText = text))
        }
    }

    data class UpdateMessageState(
        val messageState: SplashScreenState.DisplayState.MessageState
    ) : SplashScreenStateUpdate {
        override operator fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object GoToHomeScreen : SplashScreenStateUpdate {
        override operator fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(navigationState = SplashScreenState.NavigationState.HomeScreen)
        }
    }

    data object MessageConsumed : SplashScreenStateUpdate {
        override operator fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data object NavigationConsumed : SplashScreenStateUpdate {
        override operator fun invoke(oldState: SplashScreenState): SplashScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}
