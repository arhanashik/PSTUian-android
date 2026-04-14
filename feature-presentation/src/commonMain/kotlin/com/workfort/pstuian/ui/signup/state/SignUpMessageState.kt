package com.workfort.pstuian.ui.signup.state

sealed interface SignUpMessageState {
    data object SignUpSuccess : SignUpMessageState
    data class Error(val message: String) : SignUpMessageState
}
