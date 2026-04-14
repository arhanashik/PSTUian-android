package com.workfort.pstuian.ui.emailverification

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiEvent
import com.workfort.pstuian.ui.emailverification.state.EmailVerificationUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: EmailVerificationUiStateMachine,
) : UiStateMachineViewModel<EmailVerificationUiState>(stateMachine) {

    override fun onUiReady() {}

    fun onUiEvent(event: EmailVerificationUiEvent) {
        viewModelScope.launch {
            when (event) {
                is EmailVerificationUiEvent.OnClickBack -> stateMachine.onClickBack()
                is EmailVerificationUiEvent.OnClickUserTypeBtn -> stateMachine.onClickUserTypeBtn(event.userType)
                is EmailVerificationUiEvent.OnClickSignIn -> stateMachine.onClickSignIn()
                is EmailVerificationUiEvent.OnClickSendEmail -> sendVerificationEmail(event.email)
                is EmailVerificationUiEvent.MessageConsumed -> stateMachine.messageConsumed()
                is EmailVerificationUiEvent.NavigationConsumed -> stateMachine.navigationConsumed()
            }
        }
    }

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
            stateMachine.showSuccess(response)
        }.onFailure {
            val message = it.message ?: "Couldn't send verification email!"
            stateMachine.showError(message)
        }
    }
}
