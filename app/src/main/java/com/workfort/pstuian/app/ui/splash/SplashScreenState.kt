package com.workfort.pstuian.app.ui.splash


data class SplashScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val loadingText: String,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(loadingText = "", messageState = null)
        }
        sealed interface MessageState {
            data object DeviceRegFailed : MessageState
            data object GetConfigFailed : MessageState
            data object ForceUpdate : MessageState
            data object ForceRefresh : MessageState
        }
    }

    sealed interface NavigationState {
        data object HomeScreen : NavigationState
    }
}