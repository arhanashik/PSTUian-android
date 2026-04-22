package com.workfort.pstuian.ui.signin.state

sealed interface SignInNavigationState {
    data object GoBack : SignInNavigationState
}
