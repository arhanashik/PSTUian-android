package com.workfort.pstuian.app.ui.contactus

import com.workfort.pstuian.model.ContactUsInput

sealed class ContactUsScreenUiEvent {
    data object None : ContactUsScreenUiEvent()
    data object OnClickBack : ContactUsScreenUiEvent()
    data object OnClickSend : ContactUsScreenUiEvent()
    data class OnChangeContactUsInput(val input: ContactUsInput) : ContactUsScreenUiEvent()
    data object MessageConsumed : ContactUsScreenUiEvent()
    data object NavigationConsumed : ContactUsScreenUiEvent()
}