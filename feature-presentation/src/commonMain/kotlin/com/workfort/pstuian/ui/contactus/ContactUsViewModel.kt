package com.workfort.pstuian.ui.contactus

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.ContactUsInput
import com.workfort.pstuian.featuredomain.model.ContactUsInputValidationError
import com.workfort.pstuian.featuredomain.repository.SupportRepository
import com.workfort.pstuian.ui.contactus.state.ContactUsMessageState
import com.workfort.pstuian.ui.contactus.state.ContactUsNavigationState
import com.workfort.pstuian.ui.contactus.state.ContactUsUiEvent
import com.workfort.pstuian.ui.contactus.state.ContactUsUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

internal class ContactUsViewModel(
    private val repo: SupportRepository,
    private val uiStateMachine: ContactUsUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<ContactUsUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<ContactUsMessageState?>(null)
    val message: StateFlow<ContactUsMessageState?> = _message

    private val _navigation = MutableStateFlow<ContactUsNavigationState?>(null)
    val navigation: StateFlow<ContactUsNavigationState?> = _navigation

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
    }

    fun onUiEvent(event: ContactUsUiEvent) {
        when (event) {
            is ContactUsUiEvent.OnClickBack -> onClickBack()
            is ContactUsUiEvent.OnChangeInput -> onChangeContactUsInput(event.input)
            is ContactUsUiEvent.OnClickSend -> sendInquiry()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { ContactUsNavigationState.GoBack }
    }

    private fun onChangeContactUsInput(input: ContactUsInput) {
        uiStateMachine.updateInput(input)
    }

    private fun sendInquiry() {
        val state = uiState.value as? ContactUsUiState.Content ?: return
        val contactUsInput = state.input
        val validationError = contactUsInput.validate()
        uiStateMachine.updateValidationError(validationError)
        if (validationError.isNotEmpty()) {
            return
        }
        uiStateMachine.showLoading(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                repo.sendInquiry(
                    name = contactUsInput.name,
                    email = contactUsInput.email,
                    type = "query",
                    query = contactUsInput.message,
                )
            }.onSuccess { response ->
                uiStateMachine.showLoading(false)
                _message.update {
                    ContactUsMessageState.Success(response)
                }
            }.onFailure {
                uiStateMachine.showLoading(false)
                val message = it.message ?: "Failed to send the message. Please try again."
                _message.update {
                    ContactUsMessageState.Error(message)
                }
            }
        }
    }

    private fun ContactUsInput.validate(): ContactUsInputValidationError {
        return ContactUsInputValidationError(
            name = if (name.isEmpty()) "*Required" else "",
            email = if (email.isEmpty()) {
                "*Required"
            } else if (email.isValidEmail().not()) {
                "*Invalid email address"
            } else {
                ""
            },
            message = if (message.isEmpty()) {
                "*Required"
            } else if (message.length > 500) {
                "*Message too long"
            } else {
                ""
            },
        )
    }
}
