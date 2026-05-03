package com.workfort.pstuian.ui.home.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface HomeMessageState {
    data object SignInNecessary : HomeMessageState
    data object NotificationPermission : HomeMessageState
    data class ClearAllDataFailed(val error: String) : HomeMessageState
    data class UserTypeSelectionForSignIn(
        val selectedUserType: UserType?,
        val onSaveAndContinue: (UserType?) -> Unit,
    ) : HomeMessageState
    data class SignInNotSupportedForUserType(val message: String) : HomeMessageState
}
