package com.workfort.pstuian.ui.changepassword.state

sealed interface ChangePasswordMessageState {
    data class Loader(val cancelable: Boolean = false) : ChangePasswordMessageState
    data class Success(
        val message: String,
        val navigateToSignInAfterDismiss: Boolean = false,
    ) : ChangePasswordMessageState
    data class Error(val message: String) : ChangePasswordMessageState
}
