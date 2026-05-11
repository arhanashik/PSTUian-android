package com.workfort.pstuian.ui.deleteaccount.state

sealed interface DeleteAccountUiEvent {
    data object BackClicked : DeleteAccountUiEvent
    data class OnChangeInput(val input: String) : DeleteAccountUiEvent
    data class DeactivateAccountClicked(val password: String) : DeleteAccountUiEvent
    data object DeleteAccountClicked : DeleteAccountUiEvent
}
