package com.workfort.pstuian.ui.forgotpassword

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordMessageState
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordNavigationState
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiEvent
import com.workfort.pstuian.ui.forgotpassword.state.ForgotPasswordUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel (
    private val authRepo: AuthRepository,
    private val stateMachine: ForgotPasswordUiStateMachine,
) : UiStateMachineViewModel<ForgotPasswordUiState>(stateMachine) {

    private val _message = MutableStateFlow<ForgotPasswordMessageState?>(null)
    val message: StateFlow<ForgotPasswordMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<ForgotPasswordNavigationState?>(null)
    val navigation: StateFlow<ForgotPasswordNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        stateMachine.changeUserType(UserType.STUDENT)
    }

    fun onUiEvent(event: ForgotPasswordUiEvent) {
        when (event) {
            is ForgotPasswordUiEvent.BackClicked -> onClickBack()
            is ForgotPasswordUiEvent.UserTypeBtnClicked -> onClickUserTypeBtn(event.userType)
            is ForgotPasswordUiEvent.SignInClicked -> onClickSignIn()
            is ForgotPasswordUiEvent.SendResetLinkClicked -> sendPasswordResetLink(event.email)
        }
    }

    private fun onClickBack() = _navigation.update { ForgotPasswordNavigationState.GoBack }

    private fun onClickUserTypeBtn(userType: UserType) {
        stateMachine.changeUserType(userType)
    }

    private fun onClickSignIn() = _navigation.update { ForgotPasswordNavigationState.SignIn }

    private fun sendPasswordResetLink(email: String) {
        val userType = uiState.value.userType ?: return
        val validationError = if (email.isEmpty()) {
            "*Required"
        } else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            null
        }
        stateMachine.setValidationError(validationError)
        if (validationError != null) {
            return
        }
        viewModelScope.launch {
            stateMachine.showLoading(true)
            runCatching {
                val response = authRepo.forgotPassword(userType.type, email)
                stateMachine.showLoading(false)
                _message.update {
                    ForgotPasswordMessageState.PasswordResetLinkSentSuccess(message = response)
                }
            }.onFailure {
                val message = it.message ?: "Couldn't send verification email!"
                stateMachine.showLoading(false)
                _message.update {
                    ForgotPasswordMessageState.Error(message)
                }
            }
        }
    }

    fun onNavigationHandled() {
        _navigation.update { null }
    }

    fun onMessageHandled() {
        _message.update { null }
    }
}
