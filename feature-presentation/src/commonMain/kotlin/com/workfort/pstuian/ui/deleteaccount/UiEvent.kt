package com.workfort.pstuian.app.ui.common.ui.deleteaccount

sealed class DeleteAccountUiEvent {
    data object OnClickBack : DeleteAccountUiEvent()
    data object OnClickDeleteAccountBtn : DeleteAccountUiEvent()
    data class OnChangeInput(val input: String) : DeleteAccountUiEvent()
    data object OnDeleteAccount : DeleteAccountUiEvent()
    data object OnRequestRecovery : DeleteAccountUiEvent()
    data object OnResetToHomeScreen : DeleteAccountUiEvent()
    data object MessageConsumed : DeleteAccountUiEvent()
}