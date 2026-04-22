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
    private var cachedStudentSignUpFormData: SignUpFormData.StudentSignUpFormData? = null
    private var cachedTeacherSignUpFormData: SignUpFormData.TeacherSignUpFormData? = null

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
                    val updatedFormData = when (val formData = current.formData) {
                        is SignUpFormData.StudentSignUpFormData -> {
                            val updatedStudentForm = formData.copy(email = email)
                            cachedStudentSignUpFormData = updatedStudentForm
                            cachedTeacherSignUpFormData = cachedTeacherSignUpFormData
                                ?.copyFromCommonFields(updatedStudentForm)
                            updatedStudentForm
                        }
                        is SignUpFormData.TeacherSignUpFormData -> {
                            val updatedTeacherForm = formData.copy(email = email)
                            cachedTeacherSignUpFormData = updatedTeacherForm
                            cachedStudentSignUpFormData = cachedStudentSignUpFormData
                                ?.copyFromCommonFields(updatedTeacherForm)
                            updatedTeacherForm
                        }
                    }
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
                    val updatedFormData = when (val formData = current.formData) {
                        is SignUpFormData.StudentSignUpFormData -> {
                            val updatedStudentForm = formData.copy(password = password)
                            cachedStudentSignUpFormData = updatedStudentForm
                            cachedTeacherSignUpFormData = cachedTeacherSignUpFormData
                                ?.copyFromCommonFields(updatedStudentForm)
                            updatedStudentForm
                        }
                        is SignUpFormData.TeacherSignUpFormData -> {
                            val updatedTeacherForm = formData.copy(password = password)
                            cachedTeacherSignUpFormData = updatedTeacherForm
                            cachedStudentSignUpFormData = cachedStudentSignUpFormData
                                ?.copyFromCommonFields(updatedTeacherForm)
                            updatedTeacherForm
                        }
                    }
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

    fun updateSignUpFormData(formData: SignUpFormData) {
        when (formData) {
            is SignUpFormData.StudentSignUpFormData -> {
                cachedStudentSignUpFormData = formData
                cachedTeacherSignUpFormData = cachedTeacherSignUpFormData
                    ?.copyFromCommonFields(formData)
            }
            is SignUpFormData.TeacherSignUpFormData -> {
                cachedTeacherSignUpFormData = formData
                cachedStudentSignUpFormData = cachedStudentSignUpFormData
                    ?.copyFromCommonFields(formData)
            }
        }
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
                AuthPanel.StudentSignUp -> transitionToStudentSignUp(current.isLoading)
                AuthPanel.TeacherSignUp -> transitionToTeacherSignUp(current.isLoading)
                AuthPanel.ForgotPassword -> transitionToForgotPassword(current.isLoading)
                AuthPanel.EmailVerification -> transitionToEmailVerification(current.isLoading)
            }
        }
    }

    private fun transitionToSignIn(isLoading: Boolean): SignInUiState {
        val formData = cachedSignInFormData ?: SignInFormData(email = "", password = "")
        return SignInUiState.SignInPanel(isLoading = isLoading, formData = formData, rememberMe = false)
    }

    private fun transitionToStudentSignUp(isLoading: Boolean): SignInUiState {
        val formData = cachedStudentSignUpFormData ?: cachedTeacherSignUpFormData?.toStudentSignUpFormData()
            ?: SignUpFormData.StudentSignUpFormData(
                name = "",
                email = "",
                studentId = "",
                regNumber = "",
                faculty = "",
                batch = "",
                password = "",
            )
        cachedStudentSignUpFormData = formData
        return SignInUiState.SignUpPanel(isLoading, formData)
    }

    private fun transitionToTeacherSignUp(isLoading: Boolean): SignInUiState {
        val formData = cachedTeacherSignUpFormData ?: cachedStudentSignUpFormData?.toTeacherSignUpFormData()
            ?: SignUpFormData.TeacherSignUpFormData(
                name = "",
                email = "",
                faculty = "",
                department = "",
                designation = "",
                password = "",
            )
        cachedTeacherSignUpFormData = formData
        return SignInUiState.SignUpPanel(isLoading, formData)
    }

    private fun transitionToForgotPassword(isLoading: Boolean): SignInUiState {
        val email = cachedSignInFormData?.email
            ?: cachedStudentSignUpFormData?.email
            ?: cachedTeacherSignUpFormData?.email
            ?: ""
        return SignInUiState.ForgotPasswordPanel(isLoading, email)
    }

    private fun transitionToEmailVerification(isLoading: Boolean): SignInUiState {
        val email = cachedSignInFormData?.email
            ?: cachedStudentSignUpFormData?.email
            ?: cachedTeacherSignUpFormData?.email
            ?: ""
        val password = cachedSignInFormData?.password
            ?: cachedStudentSignUpFormData?.password
            ?: cachedTeacherSignUpFormData?.password
            ?: ""
        return SignInUiState.EmailVerificationPanel(isLoading, email, password)
    }
}
