package com.workfort.pstuian.ui.splash.state

sealed interface SplashUiEvent {
    data object CheckAuth : SplashUiEvent
    data object TryDeviceReg : SplashUiEvent
    data object TryGetConfig : SplashUiEvent
    data object UpdateApp : SplashUiEvent
    data object RefreshData : SplashUiEvent
    data object MessageConsumed : SplashUiEvent
    data object NavigationConsumed : SplashUiEvent
}
