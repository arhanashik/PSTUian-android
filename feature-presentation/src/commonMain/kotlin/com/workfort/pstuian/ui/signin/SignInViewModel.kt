package com.workfort.pstuian.ui.signin

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData
import com.workfort.pstuian.ui.signin.state.SignInMessageState
import com.workfort.pstuian.ui.signin.state.SignInNavigationState
import com.workfort.pstuian.ui.signin.state.SignInUiEvent
import com.workfort.pstuian.ui.signin.state.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val settingsRepository: SettingsRepository,
    private val facultyRepository: FacultyRepository,
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
            is SignInUiEvent.BackClicked -> _navigation.update { SignInNavigationState.GoBack }
            is SignInUiEvent.AuthPanelChanged -> stateMachine.setAuthPanel(event.panel)
            is SignInUiEvent.EmailChanged -> stateMachine.updateEmail(event.email)
            is SignInUiEvent.PasswordChanged -> stateMachine.updatePassword(event.password)
            is SignInUiEvent.SignInFormDataChanged -> stateMachine.updateSignInForm(event.formData)
            is SignInUiEvent.SignInRememberMeToggled -> stateMachine.toggleRememberMe(event.rememberMe)
            is SignInUiEvent.SignUpFromSignInClicked -> showSignUpPanelByUserType()
            is SignInUiEvent.SignUpUserTypeToggled -> onSignUpUserTypeToggled(event.userType)
            is SignInUiEvent.SignUpFormDataChanged -> stateMachine.updateSignUpFormData(event.formData)
            is SignInUiEvent.SignInClicked -> signIn(event.formData)
            is SignInUiEvent.StudentSignUpClicked -> studentSignUp(event.formData)
            is SignInUiEvent.TeacherSignUpClicked -> teacherSignUp(event.formData)
            is SignInUiEvent.ForgotPasswordClicked -> sendPasswordResetLink(event.email)
            is SignInUiEvent.EmailVerificationClicked -> sendVerificationEmail(event.email, event.password)
            is SignInUiEvent.TermsAndConditionsClicked -> {
                // TODO
            }
            is SignInUiEvent.PrivacyPolicyClicked -> {
                // TODO
            }
            is SignInUiEvent.SignUpFacultyPickerClicked -> openSignUpFacultySelectionSheet()
            is SignInUiEvent.SignUpBatchPickerClicked -> openSignUpBatchSelectionSheet()
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun currentSignUpFacultyId(): Int? {
        val state = uiState.value
        if (state !is SignInUiState.SignUpPanel) return null
        return when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> form.faculty?.id
            is SignUpFormData.TeacherSignUpFormData -> form.faculty?.id
        }
    }

    private fun currentSignUpBatchId(): Int? {
        val state = uiState.value
        if (state !is SignInUiState.SignUpPanel) return null
        return when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> form.batch?.id
            is SignUpFormData.TeacherSignUpFormData -> null
        }
    }

    private fun openSignUpFacultySelectionSheet() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            val faculties = facultyRepository.getFaculties(forceRefresh = false)
            if (faculties.isEmpty()) {
                _message.update {
                    SignInMessageState.Error("No faculties found. Please try again later.")
                }
            } else {
                _message.update {
                    SignInMessageState.FacultySelection(
                        faculties = faculties,
                        selectedFacultyId = currentSignUpFacultyId(),
                        onSaveAndContinue = { faculty ->
                            _message.update { null }
                            faculty?.let { applySignUpFaculty(it) }
                        },
                    )
                }
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
            val batches = facultyRepository.getBatches(facultyId, forceRefresh = false)
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
                            _message.update { null }
                            batch?.let { applySignUpBatch(it) }
                        },
                    )
                }
            }
        }
    }

    private fun applySignUpBatch(batch: BatchEntity) {
        val state = uiState.value
        if (state !is SignInUiState.SignUpPanel) return
        when (val form = state.formData) {
            is SignUpFormData.StudentSignUpFormData -> {
                stateMachine.updateSignUpFormData(form.copy(batch = batch))
            }
            is SignUpFormData.TeacherSignUpFormData -> Unit
        }
    }

    private fun applySignUpFaculty(faculty: FacultyEntity) {
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

    private fun onSignUpUserTypeToggled(userType: UserType) {
        settingsRepository.setUserType(userType)
        when (userType) {
            UserType.STUDENT -> stateMachine.setAuthPanel(AuthPanel.StudentSignUp)
            UserType.TEACHER -> stateMachine.setAuthPanel(AuthPanel.TeacherSignUp)
            UserType.EMPLOYEE -> Unit
        }
    }

    private fun sendPasswordResetLink(email: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.resetPassword(email)
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

    private fun sendVerificationEmail(email: String, password: String) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            stateMachine.showLoading(true)
            authRepository.sendVerificationEmail(email, password)
                .onSuccess {
                    stateMachine.showLoading(false)
                    _message.update {
                        SignInMessageState.Success("A verification link has been sent to $email")
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
            authRepository.signIn(formData.email, formData.password, userType)
                .onSuccess { user ->
                    stateMachine.showLoading(false)
                    _message.update { SignInMessageState.Success(message = "Welcome ${user.name}!") }
                    _navigation.update { SignInNavigationState.GoBack }
                }
                .onFailure { error ->
                    stateMachine.showLoading(false)
                    val msg = error.message ?: "Failed to Sign in. Please try again."
                    _message.update { SignInMessageState.Error(msg) }
                }
        }
    }

    private fun studentSignUp(formData: SignUpFormData.StudentSignUpFormData) {
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
            )
        }
    }

    private fun teacherSignUp(formData: SignUpFormData.TeacherSignUpFormData) {
        // TODO: wire teacher sign-up to repository once endpoint is ready.
        if (formData.isInvalid()) {
            _message.update {
                SignInMessageState.Error("Please enter valid credentials and try again")
            }
            return
        }
        _message.update {
            SignInMessageState.Success(
                message = "Teacher sign up not implemented yet (received: ${formData.email})",
            )
        }
    }
}
