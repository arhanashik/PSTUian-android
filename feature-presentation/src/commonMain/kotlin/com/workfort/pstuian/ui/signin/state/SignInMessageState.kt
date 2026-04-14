package com.workfort.pstuian.ui.signin.state

sealed interface SignInMessageState {
    data class Success(val message: String, val showToast: Boolean) : SignInMessageState
    data class Error(val message: String) : SignInMessageState
}
