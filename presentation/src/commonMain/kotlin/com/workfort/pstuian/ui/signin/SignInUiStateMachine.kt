package com.workfort.pstuian.ui.signin

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.EmailVerificationFormData
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
    private var cachedAuthUserTypeForForms: UserType = UserType.STUDENT
    private var cachedStudentSignUpFormData: SignUpFormData.StudentSignUpFormData? = null
    private var cachedTeacherSignUpFormData: SignUpFormData.TeacherSignUpFormData? = null

    fun showInitialState(
        email: String,
        rememberMe: Boolean,
        authUserTypeForForms: UserType,
    ) {
        val initialFormData = SignInFormData(email = email, password = "", rememberMe = rememberMe)

        cachedAuthUserTypeForForms = authUserTypeForForms
        cachedSignInFormData = initialFormData

        _uiState.update {
            SignInUiState.SignInPanel(
                isLoading = false,
                formData = initialFormData,
                authUserTypeForForms = authUserTypeForForms,
            )
        }
    }

    /**
     * Persists [userType] to cache and UI. When [switchSignUpChildPanel] is true (sign-up panel is
     * showing), switches student/teacher form data in the same update so the toggle and fields stay aligned.
     */
    fun applyAuthUserTypeFromToggle(userType: UserType, switchSignUpChildPanel: Boolean) {
        cachedAuthUserTypeForForms = userType
        if (switchSignUpChildPanel && (userType == UserType.STUDENT || userType == UserType.TEACHER)) {
            _uiState.update { current ->
                when (userType) {
                    UserType.STUDENT -> transitionToStudentSignUp(current.isLoading)
                    UserType.TEACHER -> transitionToTeacherSignUp(current.isLoading)
                }
            }
        } else {
            _uiState.update { current ->
                when (current) {
                    is SignInUiState.SignInPanel -> current.copy(authUserTypeForForms = userType)
                    is SignInUiState.SignUpPanel -> current.copy(authUserTypeForForms = userType)
                    else -> current
                }
            }
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

    fun updateSignInForm(formData: SignInFormData) {
        cachedSignInFormData = formData
        _uiState.update { current ->
            when (current) {
                is SignInUiState.SignInPanel -> current.copy(formData = formData)
                else -> current
            }
        }
    }

    fun updateSignUpFormData(formData: SignUpFormData) {
        when (formData) {
            is SignUpFormData.StudentSignUpFormData -> cachedStudentSignUpFormData = formData
            is SignUpFormData.TeacherSignUpFormData -> cachedTeacherSignUpFormData = formData
        }
        _uiState.update { current ->
            when (current) {
                is SignInUiState.SignUpPanel -> current.copy(formData = formData)
                else -> current
            }
        }
    }

    fun updateEmailVerificationFormData(formData: EmailVerificationFormData) {
        _uiState.update { current ->
            when (current) {
                is SignInUiState.EmailVerificationPanel -> current.copy(formData = formData)
                else -> current
            }
        }
    }

    fun updateForgotPasswordFormData(email: String) {
        _uiState.update { current ->
            when (current) {
                is SignInUiState.ForgotPasswordPanel -> current.copy(email = email)
                else -> current
            }
        }
    }

    fun setAuthPanel(panel: AuthPanel) {
        _uiState.update { current ->
            when (panel) {
                AuthPanel.SignIn -> transitionToSignIn(current.isLoading)
                AuthPanel.StudentSignUp -> transitionToStudentSignUp(current.isLoading)
                AuthPanel.TeacherSignUp -> transitionToTeacherSignUp(current.isLoading)
                AuthPanel.ForgotPassword -> transitionToForgotPassword(current.isLoading)
                AuthPanel.EmailVerification -> transitionToEmailVerification(current.isLoading)
            }
        }
    }

    private fun transitionToSignIn(isLoading: Boolean): SignInUiState {
        val formData = cachedSignInFormData ?: SignInFormData()
        return SignInUiState.SignInPanel(
            isLoading = isLoading,
            formData = formData,
            authUserTypeForForms = cachedAuthUserTypeForForms,
        )
    }

    private fun transitionToStudentSignUp(isLoading: Boolean): SignInUiState {
        val formData = cachedStudentSignUpFormData ?: SignUpFormData.StudentSignUpFormData()
        return SignInUiState.SignUpPanel(
            isLoading = isLoading,
            formData = formData,
            authUserTypeForForms = cachedAuthUserTypeForForms,
        )
    }

    private fun transitionToTeacherSignUp(isLoading: Boolean): SignInUiState {
        val formData = cachedTeacherSignUpFormData ?: SignUpFormData.TeacherSignUpFormData()
        return SignInUiState.SignUpPanel(
            isLoading = isLoading,
            formData = formData,
            authUserTypeForForms = cachedAuthUserTypeForForms,
        )
    }

    private fun transitionToForgotPassword(isLoading: Boolean): SignInUiState {
        val email = cachedSignInFormData?.email.orEmpty()
        return SignInUiState.ForgotPasswordPanel(isLoading, email)
    }

    private fun transitionToEmailVerification(isLoading: Boolean): SignInUiState {
        val formData = EmailVerificationFormData(
            email = cachedSignInFormData?.email.orEmpty(),
            password = cachedSignInFormData?.password.orEmpty(),
        )
        return SignInUiState.EmailVerificationPanel(isLoading, formData)
    }
}
