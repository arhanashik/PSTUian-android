package com.workfort.pstuian.app.ui.contactus

import com.workfort.pstuian.model.ContactUsInput
import com.workfort.pstuian.model.ContactUsInputValidationError


data class ContactUsScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val contactUsInput: ContactUsInput,
        val validationError: ContactUsInputValidationError,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                contactUsInput = ContactUsInput.INITIAL,
                validationError = ContactUsInputValidationError.INITIAL,
                messageState = null,
            )
        }
        sealed interface MessageState {
            data class SendInquirySuccess(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
    }
}