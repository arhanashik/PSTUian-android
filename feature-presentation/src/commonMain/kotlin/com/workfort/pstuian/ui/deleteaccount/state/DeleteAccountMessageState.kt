package com.workfort.pstuian.ui.deleteaccount.state

sealed interface DeleteAccountMessageState {
    data class Loading(val cancelable: Boolean) : DeleteAccountMessageState
    data object ConfirmAccountDelete : DeleteAccountMessageState
    data class Success(val message: String) : DeleteAccountMessageState
    data class Error(val message: String) : DeleteAccountMessageState
}
