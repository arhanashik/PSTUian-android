package com.workfort.pstuian.ui.changepassword.state

sealed interface ChangePasswordMessageState {
    data class Error(val message: String) : ChangePasswordMessageState
    data class Success(val message: String) : ChangePasswordMessageState
}
