package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData

sealed interface SignInUiEvent {
    data object BackClicked : SignInUiEvent
    data class AuthPanelChanged(val panel: AuthPanel) : SignInUiEvent

    // Common ui event for Email/Password, so that same change can be reflected to all Auth panel
    data class EmailChanged(val email: String) : SignInUiEvent
    data class PasswordChanged(val password: String) : SignInUiEvent
    data class SignInFormDataChanged(val formData: SignInFormData) : SignInUiEvent
    data class SignInRememberMeToggled(val rememberMe: Boolean) : SignInUiEvent
    data object SignUpFromSignInClicked : SignInUiEvent
    data class SignUpUserTypeToggled(val userType: UserType) : SignInUiEvent
    data class SignUpFormDataChanged(val formData: SignUpFormData) : SignInUiEvent

    data class SignInClicked(val formData: SignInFormData) : SignInUiEvent
    data class StudentSignUpClicked(val formData: SignUpFormData.StudentSignUpFormData) : SignInUiEvent
    data class TeacherSignUpClicked(val formData: SignUpFormData.TeacherSignUpFormData) : SignInUiEvent
    data class ForgotPasswordClicked(val email: String) : SignInUiEvent
    data class EmailVerificationClicked(val email: String, val password: String) : SignInUiEvent
    data object TermsAndConditionsClicked : SignInUiEvent
    data object PrivacyPolicyClicked : SignInUiEvent
}
