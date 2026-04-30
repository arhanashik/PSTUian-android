package com.workfort.pstuian.ui.changepassword.state

sealed interface ChangePasswordNavigationState {
    data object GoBack : ChangePasswordNavigationState
    data object SignIn : ChangePasswordNavigationState
}
