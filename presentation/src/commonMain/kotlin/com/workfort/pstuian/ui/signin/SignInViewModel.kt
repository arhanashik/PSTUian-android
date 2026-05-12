package com.workfort.pstuian.ui.signin

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.EmailVerificationFormData
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.screendata.mapToErrorMessageForSignInScreen
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
    private val facultyRepository: FacultyRepository,
    private val sharedPrefRepository: SharedPrefRepository,
    private val sharedScreenData: SharedScreenData,
    private val stateMachine: SignInUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<SignInUiState>(stateMachine) {

    private val _message = MutableStateFlow<SignInMessageState?>(null)
    val message = _message.asStateFlow()

    private val _navigation = MutableStateFlow<SignInNavigationState?>(null)
    val navigation = _navigation.asStateFlow()

    private fun coercedStudentOrTeacherUserType(): UserType =
        when (val t = settingsRepository.getUserType()) {
            UserType.STUDENT, UserType.TEACHER -> t
            else -> UserType.STUDENT
        }

    override fun onUiReady() {
        val savedEmail = sharedPrefRepository.getString(SharedPrefKey.SIGN_IN_EMAIL) ?: ""
        stateMachine.showInitialState(
            email = savedEmail,
            rememberMe = savedEmail.isNotBlank(),
            authUserTypeForForms = coercedStudentOrTeacherUserType(),
        )
    }

    fun onUiEvent(event: SignInUiEvent) {
        when (event) {
            is SignInUiEvent.BackClicked -> _navigation.update { SignInNavigationState.GoBack }

            // Auth panel
            is SignInUiEvent.AuthPanelChanged -> stateMachine.setAuthPanel(event.panel)
            is SignInUiEvent.AuthUserTypeForFormsToggled -> onAuthUserTypeForFormsToggled(event.userType)

            // Form data Changes
            is SignInUiEvent.SignInFormDataChanged -> stateMachine.updateSignInForm(event.formData)
            is SignInUiEvent.SignUpFormDataChanged -> stateMachine.updateSignUpFormData(event.formData)
            is SignInUiEvent.EmailVerificationFormDataChanged ->
                stateMachine.updateEmailVerificationFormData(event.formData)
            is SignInUiEvent.ForgotPasswordFormDataChanged -> stateMachine.updateForgotPasswordFormData(event.email)

            // Sign in panel events
            is SignInUiEvent.SignUpFromSignInClicked -> showSignUpPanelByUserType()
            is SignInUiEvent.SignInClicked -> signIn(event.formData)

            // Sign up panel events
            is SignInUiEvent.SignUpFacultyPickerClicked -> openSignUpFacultySelectionSheet()
            is SignInUiEvent.SignUpBatchPickerClicked -> openSignUpBatchSelectionSheet()
            is SignInUiEvent.TermsAndConditionsClicked -> {
                sharedScreenData.getAppConfig()?.termsAndConditionsUrl?.let { url ->
                    _message.update { SignInNavigationState.OpenWebScreen(url) }
                }
            }
            is SignInUiEvent.PrivacyPolicyClicked -> {
                sharedScreenData.getAppConfig()?.privacyPolicyUrl?.let { url ->
                    _message.update { SignInNavigationState.OpenWebScreen(url) }
                }
            }
            is SignInUiEvent.StudentSignUpClicked -> studentSignUp(event.formData)
            is SignInUiEvent.TeacherSignUpClicked -> teacherSignUp(event.formData)

            // Forgot password panel events
            is SignInUiEvent.ForgotPasswordClicked -> sendPasswordResetLink(event.email)

            // Email verification panel events
            is SignInUiEvent.EmailVerificationClicked -> sendVerificationEmail(event.formData)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun currentSignUpFacultyId(): Int? {
        val state = uiState.value as? SignInUiState.SignUpPanel ?: return null
        return when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> form.faculty?.id
            is SignUpFormData.TeacherSignUpFormData -> form.faculty?.id
        }
    }

    private fun currentSignUpBatchId(): Int? {
        val state = uiState.value as? SignInUiState.SignUpPanel ?: return null
        return when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> form.batch?.id
            is SignUpFormData.TeacherSignUpFormData -> null
        }
    }

    private fun openSignUpFacultySelectionSheet() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            facultyRepository.getFaculties()
                .onSuccess { faculties ->
                    if (faculties.isEmpty()) {
                        _message.update { SignInMessageState.Error("No faculties found. Please try again later.") }
                    } else {
                        _message.update {
                            SignInMessageState.FacultySelection(
                                faculties = faculties,
                                selectedFacultyId = currentSignUpFacultyId(),
                                onSaveAndContinue = { faculty ->
                                    onMessageHandled()
                                    faculty?.let { applySignUpFaculty(it) }
                                },
                            )
                        }
                    }
                }
                .onFailure {
                    val message = it.message ?: "Failed to load faculties. Please try again later."
                    _message.update { SignInMessageState.Error(message) }
                }
        }
    }

    private fun openSignUpBatchSelectionSheet() {
        val facultyId = currentSignUpFacultyId()
        if (facultyId == null) {
            _message.update {
                SignInMessageState.Error("Please select a faculty first.")
            }
            return
        }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            facultyRepository.getBatches(facultyId)
                .onSuccess { batches ->
                    stateMachine.showLoading(false)
                    if (batches.isEmpty()) {
                        _message.update {
                            SignInMessageState.Error("No batches found for this faculty. Please try again later.")
                        }
                    } else {
                        val selectedId = currentSignUpBatchId()
                        _message.update {
                            SignInMessageState.BatchSelection(
                                batches = batches,
                                selectedBatchId = selectedId,
                                onSaveAndContinue = { batch ->
                                    onMessageHandled()
                                    batch?.let { applySignUpBatch(it) }
                                },
                            )
                        }
                    }
                }
                .onFailure {
                    stateMachine.showLoading(false)
                    val error = it.message ?: "Failed to load data. Please try again later."
                    _message.update { SignInMessageState.Error(error) }
                }

        }
    }

    private fun applySignUpBatch(batch: Batch) {
        val state = uiState.value
        if (state !is SignInUiState.SignUpPanel) return
        when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> {
                stateMachine.updateSignUpFormData(form.copy(batch = batch))
            }
            is SignUpFormData.TeacherSignUpFormData -> Unit
        }
    }

    private fun applySignUpFaculty(faculty: Faculty) {
        val state = uiState.value
        if (state !is SignInUiState.SignUpPanel) return
        val updated = when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> form.copy(
                faculty = faculty,
                batch = if (form.faculty?.id == faculty.id) form.batch else null,
            )
            is SignUpFormData.TeacherSignUpFormData -> form.copy(
                faculty = faculty,
            )
        }
        stateMachine.updateSignUpFormData(updated)
    }

    private fun showSignUpPanelByUserType() {
        when (settingsRepository.getUserType()) {
            UserType.STUDENT -> stateMachine.setAuthPanel(AuthPanel.StudentSignUp)
            UserType.TEACHER -> stateMachine.setAuthPanel(AuthPanel.TeacherSignUp)
            UserType.EMPLOYEE -> {
                _message.update {
                    SignInMessageState.Error("Employee sign up is not available yet. Coming soon.")
                }
            }
            null -> {
                _message.update {
                    SignInMessageState.Error("Unable to determine user type. Please try again.")
                }
            }
        }
    }

    private fun onAuthUserTypeForFormsToggled(userType: UserType) {
        settingsRepository.setUserType(userType)
        val switchSignUpChildPanel = uiState.value is SignInUiState.SignUpPanel
        stateMachine.applyAuthUserTypeFromToggle(userType, switchSignUpChildPanel = switchSignUpChildPanel)
    }

    private fun sendPasswordResetLink(email: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.sendResetPasswordLink(email)
                .onSuccess {
                    stateMachine.showLoading(false)
                    _message.update {
                        SignInMessageState.Success("Password reset link request has been sent to $email")
                    }
                    stateMachine.setAuthPanel(AuthPanel.SignIn)
                }
                .onFailure { error ->
                    stateMachine.showLoading(false)
                    _message.update {
                        SignInMessageState.Error(
                            message = error.message ?: "Failed the reset password. Please retry",
                        )
                    }
                }
        }
    }

    private fun sendVerificationEmail(formData: EmailVerificationFormData) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.sendVerificationEmail(formData.email, formData.password)
                .onSuccess {
                    stateMachine.showLoading(false)
                    _message.update {
                        SignInMessageState.Success("A verification link has been sent to ${formData.email}")
                    }
                    stateMachine.setAuthPanel(AuthPanel.SignIn)
                }
                .onFailure { error ->
                    stateMachine.showLoading(false)
                    _message.update {
                        SignInMessageState.Error(
                            message = error.message ?: "Failed to send verification email. Please retry",
                        )
                    }
                }
        }
    }

    private fun signIn(formData: SignInFormData, isRetryFlow: Boolean = false) {
        val userType = settingsRepository.getUserType() ?: return

        if (formData.isInvalid()) {
            _message.update { SignInMessageState.Error("Please enter valid credentials and try again") }
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.signIn(userType, formData.email, formData.password).onSuccess { user ->
                stateMachine.showLoading(false)
                _message.update { SignInMessageState.Success(message = "Welcome Back!") }

                sharedScreenData.setCurrentUser(user)
                val savedEmail = if (formData.rememberMe) formData.email else ""
                sharedPrefRepository.putString(SharedPrefKey.SIGN_IN_EMAIL, savedEmail)

                _navigation.update { SignInNavigationState.GoBack }
            }.onFailure { error ->
                if (isRetryFlow) {
                    stateMachine.showLoading(false)
                    val msg = error.code.mapToErrorMessageForSignInScreen() ?: "Failed to Sign in. Please try again."
                    _message.update { SignInMessageState.Error(msg) }
                } else {
                    handleSignInFailure(formData, error)
                }
            }
        }
    }

    private fun handleSignInFailure(
        formData: SignInFormData,
        error: DomainError,
    ) {
        when (error.code) {
            DomainErrorCode.Auth.UserDeactivated -> {
                activateAccountAndContinueSignIn(formData)
            }
            else -> {
                stateMachine.showLoading(false)
                val msg = error.code.mapToErrorMessageForSignInScreen() ?: "Failed to Sign in. Please try again."
                _message.update { SignInMessageState.Error(msg) }
            }
        }
    }

    private fun activateAccountAndContinueSignIn(formData: SignInFormData) {
        val userType = settingsRepository.getUserType() ?: return
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            authRepository.activateAccount(userType, formData.email, formData.password)
                .onSuccess { signIn(formData, isRetryFlow = true) }
                .onFailure { error ->
                    stateMachine.showLoading(false)
                    val msg = error.code.mapToErrorMessageForSignInScreen() ?: "Failed to Sign in. Please try again."
                    _message.update { SignInMessageState.Error(msg) }
                }
        }
    }

    private fun studentSignUp(formData: SignUpFormData.StudentSignUpFormData) {
        if (formData.isInvalid()) {
            _message.update {
                SignInMessageState.Error("Please enter valid credentials and try again")
            }
            return
        }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.signUpStudent(
                formData.name,
                formData.studentId,
                formData.regNumber,
                formData.faculty?.id ?: 0, // null check is already in isInvalid check
                formData.batch?.id ?: 0, // null check is already in isInvalid check
                formData.session,
                formData.email,
                formData.password,
            ).onSuccess {
                stateMachine.showLoading(false)
                _message.update {
                    SignInMessageState.Success(
                        message = "A verification email has been sent to ${formData.email}. " +
                                "Please check spam folder if you can't find it in inbox.",
                    )
                }
                stateMachine.setAuthPanel(AuthPanel.SignIn)
            }.onFailure { error ->
                stateMachine.showLoading(false)
                val msg = error.code.mapToErrorMessageForSignInScreen() ?: "Failed to Sign up. Please try again."
                _message.update { SignInMessageState.Error(msg) }
            }
        }
    }

    private fun teacherSignUp(formData: SignUpFormData.TeacherSignUpFormData) {
        if (formData.isInvalid()) {
            _message.update {
                SignInMessageState.Error("Please enter valid credentials and try again")
            }
            return
        }
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.signUpTeacher(
                formData.name,
                formData.faculty?.id ?: 0, // null check is already in isInvalid check
                formData.designation,
                formData.department,
                formData.email,
                formData.password,
            ).onSuccess {
                stateMachine.showLoading(false)
                _message.update {
                    SignInMessageState.Success(
                        message = "A verification email has been sent to ${formData.email}. " +
                                "Please check spam folder if you can't find it in inbox.",
                    )
                }
                stateMachine.setAuthPanel(AuthPanel.SignIn)
            }.onFailure { error ->
                stateMachine.showLoading(false)
                val msg = error.code.mapToErrorMessageForSignInScreen() ?: "Failed to Sign up. Please try again."
                _message.update { SignInMessageState.Error(msg) }
            }
        }
    }
}
