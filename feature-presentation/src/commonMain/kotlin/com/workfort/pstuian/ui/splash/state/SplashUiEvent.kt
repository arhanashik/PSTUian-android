package com.workfort.pstuian.ui.splash.state

sealed interface SplashUiEvent {
    data class ActionBtnClicked(val isForceUpdateAction: Boolean) : SplashUiEvent
}
