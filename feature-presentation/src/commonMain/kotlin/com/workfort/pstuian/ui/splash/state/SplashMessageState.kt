package com.workfort.pstuian.ui.splash.state

sealed interface SplashMessageState {
    data class DeviceRegFailed(val onRetry: () -> Unit) : SplashMessageState
    data class GetConfigFailed(val onRetry: () -> Unit) : SplashMessageState
    data class ForceUpdate(val onConfirm: () -> Unit) : SplashMessageState
    data class ForceRefresh(val onConfirm: () -> Unit) : SplashMessageState
}
