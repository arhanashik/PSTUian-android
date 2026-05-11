package com.workfort.pstuian.ui.signin.state

sealed interface SignInNavigationState {
    data object GoBack : SignInNavigationState
    data class OpenWebScreen(val url: String) : SignInMessageState
}
