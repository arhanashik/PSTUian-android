package com.workfort.pstuian.ui.splash.state

import com.workfort.pstuian.featuredomain.model.AppUsageRole

sealed interface SplashUiEvent {
    data class ActionBtnClicked(val isForceUpdateAction: Boolean) : SplashUiEvent
    data class SelectAppUsageRole(val role: AppUsageRole) : SplashUiEvent
    data object SaveAppUsageRoleAndContinue : SplashUiEvent
}
