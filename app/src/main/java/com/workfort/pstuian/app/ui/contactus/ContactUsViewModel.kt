package com.workfort.pstuian.app.ui.contactus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ContactUsInput
import com.workfort.pstuian.model.ContactUsInputValidationError
import com.workfort.pstuian.repository.SupportRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactUsViewModel(
    private val repo: SupportRepository,
    private val reducer: ContactUsScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<ContactUsScreenState> get() = _screenState

    private fun updateScreenState(update: ContactUsScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(ContactUsScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(ContactUsScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        ContactUsScreenStateUpdate.NavigateTo(
            ContactUsScreenState.NavigationState.GoBack,
        ),
    )

    fun onChangeContactUsInput(input: ContactUsInput) {
        updateScreenState(ContactUsScreenStateUpdate.SetContactUsInput(input))
    }

    fun sendInquiry() {
        val contactUsInput = _screenState.value.displayState.contactUsInput
        val validationError = contactUsInput.validate()
        updateScreenState(ContactUsScreenStateUpdate.SetValidationError(validationError))
        if (validationError.isNotEmpty()) {
            return
        }
        updateScreenState(ContactUsScreenStateUpdate.ShowLoading(true))
        viewModelScope.launch {
            runCatching {
                val response = repo.sendInquiry(
                    name = contactUsInput.name,
                    email = contactUsInput.email,
                    type = "query",
                    query = contactUsInput.message,
                )
                updateScreenState(ContactUsScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    ContactUsScreenStateUpdate.UpdateMessageState(
                        ContactUsScreenState.DisplayState.MessageState.SendInquirySuccess(response),
                    ),
                )
            }.onFailure {
                updateScreenState(ContactUsScreenStateUpdate.ShowLoading(false))
                val message = it.message ?: "Failed to send the message. Please try again."
                updateScreenState(
                    ContactUsScreenStateUpdate.UpdateMessageState(
                        ContactUsScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun ContactUsInput.validate(): ContactUsInputValidationError {
        return ContactUsInputValidationError(
            name = if (name.isEmpty()) "*Required" else "",
            email = if (email.isEmpty()) {
                "*Required"
            }  else if (email.isValidEmail().not()) {
                "*Invalid email address"
            } else {
                ""
            },
            message = if (message.isEmpty()) {
                "*Required"
            }  else if (message.length > 500) {
                "*Message too long"
            } else {
                ""
            },
        )
    }
}