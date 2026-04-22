package com.workfort.pstuian.ui.signin

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.state.SignInMessageState
import com.workfort.pstuian.ui.signin.state.SignInNavigationState
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val stateMachine: SignInUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SignInUiState>(stateMachine) {

    private val _message = MutableStateFlow<SignInMessageState?>(null)
    val message = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SignInNavigationState?>(null)
    val navigation = _navigation.asStateFlow()

    override fun onUiReady() {
        stateMachine.showInitialState(
            email = "", // TODO get saved email from shared pref
            rememberMe = false, // TODO get rememberMe saved value
        )
    }

    fun onUiEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.BackClicked -> {
                _navigation.update { SignInNavigationState.GoBack(isSignedIn = false) }
            }
            is SignInUiEvent.AuthPanelChanged -> stateMachine.setAuthPanel(event.panel)
            is SignInUiEvent.EmailChanged -> stateMachine.updateEmail(event.email)
            is SignInUiEvent.PasswordChanged -> stateMachine.updatePassword(event.password)
            is SignInUiEvent.SignInFormDataChanged -> stateMachine.updateSignInForm(event.formData)
            is SignInUiEvent.SignInRememberMeToggled -> stateMachine.toggleRememberMe(event.rememberMe)
            is SignInUiEvent.SignUpFormDataChanged -> stateMachine.updateSignUpForm(event.formData)
            is SignInUiEvent.SignInClicked -> signIn(event.formData)
            is SignInUiEvent.SignUpClicked -> signUp(event.formData)
            is SignInUiEvent.ForgotPasswordClicked -> sendPasswordResetLink(event.email)
            is SignInUiEvent.EmailVerificationClicked -> sendVerificationEmail(event.email)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun sendPasswordResetLink(email: String) {
        // TODO: wire to AuthRepository.sendPasswordResetLink once available.
        _message.update {
            SignInMessageState.Success(
                message = "Password reset link request stubbed (for: $email)",
                showToast = true,
            )
        }
    }

    private fun sendVerificationEmail(email: String) {
        // TODO: wire to AuthRepository / Firebase sendEmailVerification once available.
        _message.update {
            SignInMessageState.Success(
                message = "Verification email request stubbed (for: $email)",
                showToast = true,
            )
        }
    }

    private fun signIn(formData: SignInFormData) {
        val userType = settingsRepository.getUserType() ?: return

        if (formData.isInvalid()) {
            _message.update {
                SignInMessageState.Error("Please enter valid credentials and try again")
            }
            return
        }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            runCatching {
                authRepository.signIn(formData.email, formData.password, userType)
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

    private fun signUp(formData: SignUpFormData) {
        // TODO: wire to repository once the sign-up endpoint is ready. For now we just surface a
        // placeholder message so the UI flow can be exercised end-to-end.
        if (formData.isInvalid()) {
            _message.update {
                SignInMessageState.Error("Please enter valid credentials and try again")
            }
            return
        }
        _message.update {
            SignInMessageState.Success(
                message = "Sign up not implemented yet (received: ${formData.email})",
                showToast = true,
            )
        }
    }
}
