package com.workfort.pstuian.ui.contactus.state

import com.workfort.pstuian.featuredomain.model.ContactUsInput

sealed interface ContactUsUiEvent {
    data object OnClickBack : ContactUsUiEvent
    data class OnChangeInput(val input: ContactUsInput) : ContactUsUiEvent
    data object OnClickSend : ContactUsUiEvent
}
