package com.workfort.pstuian.ui.signup

import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType

data class SignUpUiState(
    val isLoading: Boolean = false,
    val userType: UserType = UserType.STUDENT,
    val studentSignUpInput: StudentSignUpInput = StudentSignUpInput.INITIAL,
    val studentSignUpInputValidationError: StudentSignUpInputValidationError = StudentSignUpInputValidationError.INITIAL,
    val teacherSignUpInput: TeacherSignUpInput = TeacherSignUpInput.INITIAL,
    val teacherSignUpInputValidationError: TeacherSignUpInputValidationError = TeacherSignUpInputValidationError.INITIAL,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
)
