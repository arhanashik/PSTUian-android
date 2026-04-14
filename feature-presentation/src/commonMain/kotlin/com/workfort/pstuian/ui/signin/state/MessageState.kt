package com.workfort.pstuian.ui.signin.state

sealed interface MessageState {
    data class Success(val message: String, val showToast: Boolean) : MessageState
    data class Error(val message: String) : MessageState
}
