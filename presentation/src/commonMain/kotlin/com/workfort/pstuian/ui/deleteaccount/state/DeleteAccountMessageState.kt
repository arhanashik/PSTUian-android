package com.workfort.pstuian.ui.deleteaccount.state

sealed interface DeleteAccountMessageState {
    data class Loading(val cancelable: Boolean) : DeleteAccountMessageState
    data class ConfirmAction(val message: String, val onConfirm: () -> Unit) : DeleteAccountMessageState
    data class Error(val message: String) : DeleteAccountMessageState
}
