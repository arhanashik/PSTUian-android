package com.workfort.pstuian.app.ui.deleteaccount

sealed class DeleteAccountScreenUiEvent {
    data object None : DeleteAccountScreenUiEvent()
    data object OnClickBack : DeleteAccountScreenUiEvent()
    data object OnClickDeleteAccountBtn : DeleteAccountScreenUiEvent()
    data class OnChangeInput(val input: String) : DeleteAccountScreenUiEvent()
    data object OnDeleteAccount : DeleteAccountScreenUiEvent()
    data object OnRequestRecovery : DeleteAccountScreenUiEvent()
    data object OnResetToHomeScreen : DeleteAccountScreenUiEvent()
    data object MessageConsumed : DeleteAccountScreenUiEvent()
    data object NavigationConsumed : DeleteAccountScreenUiEvent()
}