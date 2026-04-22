package com.workfort.pstuian.ui.signin

import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.state.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInUiStateMachine : UiStateMachine<SignInUiState> {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.None())
    override val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private var cachedSignInFormData: SignInFormData? = null
    private var cachedSignUpFormData: SignUpFormData? = null

    fun showInitialState(
        email: String,
        rememberMe: Boolean,
    ) {
        val initialFormData = SignInFormData(email = email, password = "")
        cachedSignInFormData = initialFormData

        _uiState.update {
            SignInUiState.SignInPanel(
                isLoading = false,
                formData = initialFormData,
                rememberMe = rememberMe,
            )
        }
    }

    fun showLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is SignInUiState.None -> current
                is SignInUiState.SignInPanel -> current.copy(isLoading = isLoading)
                is SignInUiState.SignUpPanel -> current.copy(isLoading = isLoading)
                is SignInUiState.ForgotPasswordPanel -> current.copy(isLoading = isLoading)
                is SignInUiState.EmailVerificationPanel -> current.copy(isLoading = isLoading)
            }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { current ->
            when (current) {
                is SignInUiState.None -> current
                is SignInUiState.SignInPanel -> {
                    val updatedFormData = current.formData.copy(email = email)
                    cachedSignInFormData = updatedFormData
                    current.copy(formData = updatedFormData)
                }
                is SignInUiState.SignUpPanel -> {
                    val updatedFormData = current.formData.copy(email = email)
                    cachedSignUpFormData = updatedFormData
                    current.copy(formData = updatedFormData)
                }
                is SignInUiState.ForgotPasswordPanel -> current.copy(email = email)
                is SignInUiState.EmailVerificationPanel -> current.copy(email = email)
            }
        }
    }

    fun updatePassword(password: String) {
        _uiState.update { current ->
            when (current) {
                is SignInUiState.SignInPanel -> {
                    val updatedFormData = current.formData.copy(password = password)
                    cachedSignInFormData = updatedFormData
                    current.copy(formData = updatedFormData)
                }
                is SignInUiState.SignUpPanel -> {
                    val updatedFormData = current.formData.copy(password = password)
                    cachedSignUpFormData = updatedFormData
                    current.copy(formData = updatedFormData)
                }
                else -> current
            }
        }
    }

    fun updateSignInForm(formData: SignInFormData) {
        cachedSignInFormData = formData
        _uiState.update { current ->
            when (current) {
                is SignInUiState.SignInPanel -> current.copy(formData = formData)
                else -> current
            }
        }
    }

    fun toggleRememberMe(rememberMe: Boolean) {
        _uiState.update { current ->
            when (current) {
                is SignInUiState.SignInPanel -> current.copy(rememberMe = rememberMe)
                else -> current
            }
        }
    }

    fun updateSignUpForm(formData: SignUpFormData) {
        cachedSignUpFormData = formData
        _uiState.update { current ->
            when (current) {
                is SignInUiState.SignUpPanel -> current.copy(formData = formData)
                else -> current
            }
        }
    }

    fun setAuthPanel(panel: AuthPanel) {
        _uiState.update { current ->
            when (panel) {
                AuthPanel.SignIn -> transitionToSignIn(current.isLoading)
                AuthPanel.SignUp -> transitionToSignUp(current.isLoading)
                AuthPanel.ForgotPassword -> transitionToForgotPassword(current.isLoading)
                AuthPanel.EmailVerification -> transitionToEmailVerification(current.isLoading)
            }
        }
    }

    private fun transitionToSignIn(isLoading: Boolean): SignInUiState {
        val formData = cachedSignInFormData ?: SignInFormData(email = "", password = "")
        return SignInUiState.SignInPanel(isLoading = isLoading, formData = formData, rememberMe = false)
    }

    private fun transitionToSignUp(isLoading: Boolean): SignInUiState {
        val formData = cachedSignUpFormData ?: SignUpFormData(
            name = "",
            email = "",
            studentId = "",
            regNumber = "",
            faculty = "",
            batch = "",
            password = "",
        )
        return SignInUiState.SignUpPanel(isLoading = isLoading, formData = formData)
    }

    private fun transitionToForgotPassword(isLoading: Boolean): SignInUiState {
        val email = cachedSignInFormData?.email ?: cachedSignUpFormData?.email ?: ""
        return SignInUiState.ForgotPasswordPanel(isLoading = isLoading, email = email)
    }

    private fun transitionToEmailVerification(isLoading: Boolean): SignInUiState {
        val email = cachedSignInFormData?.email ?: cachedSignUpFormData?.email ?: ""
        return SignInUiState.EmailVerificationPanel(isLoading = isLoading, email = email)
    }
}
