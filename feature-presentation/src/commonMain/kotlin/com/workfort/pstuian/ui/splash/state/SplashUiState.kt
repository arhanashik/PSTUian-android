package com.workfort.pstuian.ui.splash.state

import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.usecase.InitialScreenState

data class SplashUiState(
    val screenState: InitialScreenState? = null,
    val statusText: String = "",
    val descriptionText: String? = null,
    val actionBtnText: String? = null,
    val showAppUsageRolePicker: Boolean = false,
    val selectedAppUsageRole: AppUsageRole? = null,
)
