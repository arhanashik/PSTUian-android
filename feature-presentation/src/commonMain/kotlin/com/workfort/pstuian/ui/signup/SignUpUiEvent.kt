package com.workfort.pstuian.ui.signup

import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.UserType

sealed interface SignUpUiEvent {
    data object None : SignUpUiEvent
    data object OnClickBack : SignUpUiEvent
    data class OnClickUserTypeBtn(val userType: UserType) : SignUpUiEvent
    data object OnClickSignUpStudent : SignUpUiEvent
    data object OnClickSignUpTeacher : SignUpUiEvent
    data object OnClickFaculty : SignUpUiEvent
    data object OnClickBatch : SignUpUiEvent
    data object OnClickSignIn : SignUpUiEvent
    data object OnClickTermsAndConditions : SignUpUiEvent
    data object OnClickPrivacyPolicy : SignUpUiEvent
    data class OnChangeFaculty(val facultyId: Int?) : SignUpUiEvent
    data class OnChangeBatch(val batchId: Int?) : SignUpUiEvent
    data class OnChangeStudentSignUpInput(val signUpInput: StudentSignUpInput) : SignUpUiEvent
    data class OnChangeTeacherSignUpInput(val signUpInput: TeacherSignUpInput) : SignUpUiEvent
    data object MessageConsumed : SignUpUiEvent
    data object NavigationConsumed : SignUpUiEvent
}
