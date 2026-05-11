package com.workfort.pstuian.ui.signin.state

import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.signin.screendata.AuthPanel
import com.workfort.pstuian.ui.signin.screendata.EmailVerificationFormData
import com.workfort.pstuian.ui.signin.screendata.SignInFormData
import com.workfort.pstuian.ui.signin.screendata.SignUpFormData

sealed interface SignInUiEvent {
    data object BackClicked : SignInUiEvent

    data class AuthPanelChanged(val panel: AuthPanel) : SignInUiEvent
    data class AuthUserTypeForFormsToggled(val userType: UserType) : SignInUiEvent

    data class SignInFormDataChanged(val formData: SignInFormData) : SignInUiEvent
    data class SignUpFormDataChanged(val formData: SignUpFormData) : SignInUiEvent
    data class EmailVerificationFormDataChanged(val formData: EmailVerificationFormData) : SignInUiEvent
    data class ForgotPasswordFormDataChanged(val email: String) : SignInUiEvent

    data object SignUpFromSignInClicked : SignInUiEvent
    data class SignInClicked(val formData: SignInFormData) : SignInUiEvent

    data object SignUpFacultyPickerClicked : SignInUiEvent
    data object SignUpBatchPickerClicked : SignInUiEvent
    data object TermsAndConditionsClicked : SignInUiEvent
    data object PrivacyPolicyClicked : SignInUiEvent
    data class StudentSignUpClicked(val formData: SignUpFormData.StudentSignUpFormData) : SignInUiEvent
    data class TeacherSignUpClicked(val formData: SignUpFormData.TeacherSignUpFormData) : SignInUiEvent

    data class ForgotPasswordClicked(val email: String) : SignInUiEvent
    data class EmailVerificationClicked(val formData: EmailVerificationFormData) : SignInUiEvent
}
