package com.workfort.pstuian.ui.emailverification

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationMessageState
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationNavigationState
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiEvent
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EmailVerificationViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: EmailVerificationUiStateMachine,
) : UiStateMachineViewModel<EmailVerificationUiState>(stateMachine) {

    private val _message = MutableStateFlow<EmailVerificationMessageState?>(null)
    val message: StateFlow<EmailVerificationMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<EmailVerificationNavigationState?>(null)
    val navigation: StateFlow<EmailVerificationNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {}

    fun onUiEvent(event: EmailVerificationUiEvent) {
        viewModelScope.launch {
            when (event) {
                is EmailVerificationUiEvent.OnClickBack -> onClickBack()
                is EmailVerificationUiEvent.OnClickUserTypeBtn -> stateMachine.onClickUserTypeBtn(event.userType)
                is EmailVerificationUiEvent.OnClickSignIn -> onClickBack()
                is EmailVerificationUiEvent.OnClickSendEmail -> sendVerificationEmail(event.email)
                is EmailVerificationUiEvent.MessageConsumed -> onMessageHandled()
                is EmailVerificationUiEvent.NavigationConsumed -> onNavigationConsumed()
            }
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationConsumed() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { EmailVerificationNavigationState.GoBack }

    private suspend fun sendVerificationEmail(email: String) {
        val validationError = if (email.isEmpty()) {
            "*Required"
        } else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            null
        }

        if (validationError != null) {
            stateMachine.updateValidationError(validationError)
            return
        }

        stateMachine.updateValidationError(null)
        stateMachine.updateLoading(true)

        runCatching {
            authRepo.emailVerification(stateMachine.uiState.value.userType.type, email)
        }.onSuccess { response ->
            stateMachine.updateLoading(false)
            _message.update { EmailVerificationMessageState.EmailSentSuccess(response) }
        }.onFailure {
            stateMachine.updateLoading(false)
            val message = it.message ?: "Couldn't send verification email!"
            _message.update { EmailVerificationMessageState.Error(message) }
        }
    }
}
