package com.workfort.pstuian.ui.splash.state

sealed interface SplashNavigationState {
    data object HomeScreen : SplashNavigationState
    data class OpenUrl(val url: String) : SplashNavigationState
}
