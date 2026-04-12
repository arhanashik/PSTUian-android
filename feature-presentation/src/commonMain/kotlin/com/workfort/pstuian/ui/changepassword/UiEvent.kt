package com.workfort.pstuian.app.ui.common.ui.changepassword

import com.workfort.pstuian.model.ChangePasswordInput

sealed class ChangePasswordUiEvent {
    data object OnClickBack : ChangePasswordUiEvent()
    data object OnClickSaveBtn : ChangePasswordUiEvent()
    data class OnChangeInput(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data object MessageConsumed : ChangePasswordUiEvent()
}