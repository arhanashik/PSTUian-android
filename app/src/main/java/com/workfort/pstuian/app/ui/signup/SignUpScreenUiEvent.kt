package com.workfort.pstuian.app.ui.signup

import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.UserType

sealed class SignUpScreenUiEvent {
    data object None : SignUpScreenUiEvent()
    data object OnClickBack : SignUpScreenUiEvent()
    data class OnClickUserTypeBtn(val userType: UserType) : SignUpScreenUiEvent()
    data object OnClickSignUpStudent : SignUpScreenUiEvent()
    data object OnClickSignUpTeacher : SignUpScreenUiEvent()
    data object OnClickFaculty : SignUpScreenUiEvent()
    data object OnClickBatch : SignUpScreenUiEvent()
    data object OnClickSignIn : SignUpScreenUiEvent()
    data object OnClickTermsAndConditions : SignUpScreenUiEvent()
    data object OnClickPrivacyPolicy : SignUpScreenUiEvent()
    data class OnChangeFaculty(val facultyId: Int) : SignUpScreenUiEvent()
    data class OnChangeBatch(val batchId: Int) : SignUpScreenUiEvent()
    data class OnChangeStudentSignUpInput(val signUpInput: StudentSignUpInput) : SignUpScreenUiEvent()
    data class OnChangeTeacherSignUpInput(val signUpInput: TeacherSignUpInput) : SignUpScreenUiEvent()
    data object MessageConsumed : SignUpScreenUiEvent()
    data object NavigationConsumed : SignUpScreenUiEvent()
}