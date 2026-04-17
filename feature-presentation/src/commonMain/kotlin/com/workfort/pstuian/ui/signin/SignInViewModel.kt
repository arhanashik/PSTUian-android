package com.workfort.pstuian.ui.signin

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.signin.state.SignInMessageState
import com.workfort.pstuian.ui.signin.state.SignInNavigationState
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel(
    private val authRepo: AuthRepository,
    private val stateMachine: SignInUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SignInUiState>(stateMachine) {

    private val _message = MutableStateFlow<SignInMessageState?>(null)
    val message = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SignInNavigationState?>(null)
    val navigation = _navigation.asStateFlow()

    override fun onUiReady() {
        // No initial data needed
    }

    fun onUiEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.BackClicked -> {
                _navigation.update { SignInNavigationState.GoBack(isSignedIn = false) }
            }
            is SignInUiEvent.UserTypeBtnClicked -> {
                stateMachine.updateUserType(event.userType)
            }
            is SignInUiEvent.SignInClicked -> {
                signIn(event.email, event.password)
            }
            is SignInUiEvent.ForgotPasswordClicked -> {
                _navigation.update { SignInNavigationState.GoToForgotPasswordScreen }
            }
            is SignInUiEvent.SignUpClicked -> {
                _navigation.update { SignInNavigationState.GoToSignUpScreen }
            }
            is SignInUiEvent.EmailVerificationClicked -> {
                _navigation.update { SignInNavigationState.GoToEmailVerificationScreen }
            }
            is SignInUiEvent.MessageConsumed -> onMessageHandled()
            is SignInUiEvent.NavigationConsumed -> onNavigationHandled()
        }
    }

    private fun signIn(email: String, password: String) {
        val userType = uiState.value.userType
        if (email.isEmpty() || email.isValidEmail().not() || password.isEmpty() || password.length < 4) {
            _message.update {
                SignInMessageState.Error("Please enter valid credentials and try again")
            }
            return
        }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            runCatching {
                authRepo.signIn(email, password, userType.type)
            }.onSuccess {
                stateMachine.showLoading(false)
                _message.update {
                    SignInMessageState.Success(message = "Signed in successfully!", showToast = true)
                }
                _navigation.update { SignInNavigationState.GoBack(isSignedIn = true) }
            }.onFailure {
                val msg = it.message ?: "Failed to Sign in. Please try again."
                stateMachine.showLoading(false)
                _message.update { SignInMessageState.Error(msg) }
            }
        }
    }

    fun onMessageHandled() {
        _message.update { null }
    }

    fun onNavigationHandled() {
        _navigation.update { null }
    }
}
