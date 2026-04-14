package com.workfort.pstuian.ui.splash.state

sealed interface SplashMessageState {
    data object DeviceRegFailed : SplashMessageState
    data object GetConfigFailed : SplashMessageState
    data object ForceUpdate : SplashMessageState
    data object ForceRefresh : SplashMessageState
}
