package com.workfort.pstuian.ui.changepassword.state

import com.workfort.pstuian.featuredomain.model.ChangePasswordInput

sealed class ChangePasswordUiEvent {
    data object BackClicked : ChangePasswordUiEvent()
    data class InputChanged(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data object ChangePasswordClicked : ChangePasswordUiEvent()
}
