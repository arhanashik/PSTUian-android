package com.workfort.pstuian.ui.emailverification.state

sealed interface EmailVerificationMessageState {
    data class EmailSentSuccess(val message: String) : EmailVerificationMessageState
    data class Error(val message: String) : EmailVerificationMessageState
}
