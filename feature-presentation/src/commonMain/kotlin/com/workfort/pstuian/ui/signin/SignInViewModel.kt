package com.workfort.pstuian.ui.signin

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.signin.state.MessageState
import com.workfort.pstuian.ui.signin.state.NavigationState
import com.workfort.pstuian.ui.signin.state.SignInUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.launch

internal class SignInViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: SignInUiStateMachine,
) : UiStateMachineViewModel<SignInUiState>(stateMachine) {

    override fun onUiReady() {
        // No initial data needed
    }

    fun onEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.OnClickBack -> onClickBack()
            is SignInUiEvent.OnClickUserTypeBtn -> onClickUserTypeBtn(event.userType)
            is SignInUiEvent.OnClickSignIn -> signIn(event.email, event.password)
            is SignInUiEvent.OnClickForgotPassword -> onClickForgotPassword()
            is SignInUiEvent.OnClickSignUp -> onClickSignUp()
            is SignInUiEvent.OnClickEmailVerification -> onClickEmailVerification()
            is SignInUiEvent.MessageConsumed -> messageConsumed()
            is SignInUiEvent.NavigationConsumed -> navigationConsumed()
        }
    }

    private fun onClickBack() {
        stateMachine.navigateTo(NavigationState.GoBack(isSignedIn = false))
    }

    private fun onClickUserTypeBtn(userType: UserType) {
        stateMachine.updateUserType(userType)
    }

    private fun onClickForgotPassword() {
        stateMachine.navigateTo(NavigationState.GoToForgotPasswordScreen)
    }

    private fun onClickSignUp() {
        stateMachine.navigateTo(NavigationState.GoToSignUpScreen)
    }

    private fun onClickEmailVerification() {
        stateMachine.navigateTo(NavigationState.GoToEmailVerificationScreen)
    }

    private fun signIn(email: String, password: String) {
        val userType = uiState.value.userType
        if (email.isEmpty() || email.isValidEmail().not() || password.isEmpty() || password.length < 4) {
            stateMachine.showMessage(
                MessageState.Error("Please enter valid credentials and try again")
            )
            return
        }
        viewModelScope.launch {
            stateMachine.showLoading(true)
            runCatching {
                authRepo.signIn(email, password, userType.type)
            }.onSuccess {
                stateMachine.showLoading(false)
                stateMachine.showMessage(
                    MessageState.Success(message = "Signed in successfully!", showToast = true)
                )
                stateMachine.navigateTo(NavigationState.GoBack(isSignedIn = true))
            }.onFailure {
                val msg = it.message ?: "Failed to Sign in. Please try again."
                stateMachine.showLoading(false)
                stateMachine.showMessage(MessageState.Error(msg))
            }
        }
    }

    override fun messageConsumed() {
        stateMachine.showMessage(null)
    }

    override fun navigationConsumed() {
        stateMachine.navigateTo(null)
    }
}
