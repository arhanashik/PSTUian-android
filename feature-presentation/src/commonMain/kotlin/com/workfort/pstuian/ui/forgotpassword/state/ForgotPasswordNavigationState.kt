package com.workfort.pstuian.ui.forgotpassword.state

sealed interface ForgotPasswordNavigationState {
    data object GoBack : ForgotPasswordNavigationState
    data object SignIn : ForgotPasswordNavigationState
}
