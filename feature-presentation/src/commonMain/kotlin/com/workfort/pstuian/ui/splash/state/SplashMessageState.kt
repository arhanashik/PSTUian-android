package com.workfort.pstuian.ui.splash.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface SplashMessageState {
    data class UserTypeSelection(
        val selectedUserType: UserType?,
        val onSaveAndContinue: (UserType?) -> Unit,
    ) : SplashMessageState
}
