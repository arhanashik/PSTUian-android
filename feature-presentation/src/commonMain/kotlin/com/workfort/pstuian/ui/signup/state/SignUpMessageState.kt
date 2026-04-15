package com.workfort.pstuian.ui.signup.state

sealed interface SignUpMessageState {
    data class SignUpSuccess(val onConfirm: () -> Unit) : SignUpMessageState
    data class Error(val message: String) : SignUpMessageState
}
