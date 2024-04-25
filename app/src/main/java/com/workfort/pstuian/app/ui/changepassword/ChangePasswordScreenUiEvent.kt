package com.workfort.pstuian.app.ui.changepassword

import com.workfort.pstuian.model.ChangePasswordInput

sealed class ChangePasswordScreenUiEvent {
    data object None : ChangePasswordScreenUiEvent()
    data object OnClickBack : ChangePasswordScreenUiEvent()
    data object OnClickSaveBtn : ChangePasswordScreenUiEvent()
    data class OnChangeInput(val input: ChangePasswordInput) : ChangePasswordScreenUiEvent()
    data object MessageConsumed : ChangePasswordScreenUiEvent()
    data object NavigationConsumed : ChangePasswordScreenUiEvent()
}