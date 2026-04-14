package com.workfort.pstuian.ui.signup

sealed interface MessageState {
    data object SignUpSuccess : MessageState
    data class Error(val message: String) : MessageState
}
