package com.workfort.pstuian.ui.changepassword.state

import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordInput
import com.workfort.pstuian.ui.changepassword.screendata.ChangePasswordScreenPanel

sealed class ChangePasswordUiEvent {
    data object BackClicked : ChangePasswordUiEvent()
    data class PanelChanged(val panel: ChangePasswordScreenPanel) : ChangePasswordUiEvent()
    data class ResetEmailChanged(val email: String) : ChangePasswordUiEvent()
    data class SendPasswordResetClicked(val email: String) : ChangePasswordUiEvent()
    data class ChangePasswordInputChanged(val input: ChangePasswordInput) : ChangePasswordUiEvent()
    data class ChangePasswordClicked(val input: ChangePasswordInput) : ChangePasswordUiEvent()
}
