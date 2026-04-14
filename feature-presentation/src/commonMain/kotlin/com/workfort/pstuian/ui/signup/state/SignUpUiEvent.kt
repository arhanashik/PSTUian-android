package com.workfort.pstuian.ui.signup.state

import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.UserType

sealed interface SignUpUiEvent {
    data object BackClicked : SignUpUiEvent
    data class UserTypeBtnClicked(val userType: UserType) : SignUpUiEvent
    data object SignUpStudentClicked : SignUpUiEvent
    data object SignUpTeacherClicked : SignUpUiEvent
    data object FacultyClicked : SignUpUiEvent
    data object BatchClicked : SignUpUiEvent
    data object SignInClicked : SignUpUiEvent
    data object TermsAndConditionsClicked : SignUpUiEvent
    data object PrivacyPolicyClicked : SignUpUiEvent
    data class FacultyChanged(val facultyId: Int?) : SignUpUiEvent
    data class BatchChanged(val batchId: Int?) : SignUpUiEvent
    data class StudentSignUpInputChanged(val signUpInput: StudentSignUpInput) : SignUpUiEvent
    data class TeacherSignUpInputChanged(val signUpInput: TeacherSignUpInput) : SignUpUiEvent
}
