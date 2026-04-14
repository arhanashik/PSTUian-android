package com.workfort.pstuian.ui.signup.state

import com.workfort.pstuian.featuredomain.model.StudentSignUpInput
import com.workfort.pstuian.featuredomain.model.StudentSignUpInputValidationError
import com.workfort.pstuian.featuredomain.model.TeacherSignUpInput
import com.workfort.pstuian.featuredomain.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.featuredomain.model.UserType

data class SignUpUiState(
    val isLoading: Boolean = false,
    val userType: UserType = UserType.STUDENT,
    val studentSignUpInput: StudentSignUpInput = StudentSignUpInput.INITIAL,
    val studentSignUpInputValidationError: StudentSignUpInputValidationError = StudentSignUpInputValidationError.INITIAL,
    val teacherSignUpInput: TeacherSignUpInput = TeacherSignUpInput.INITIAL,
    val teacherSignUpInputValidationError: TeacherSignUpInputValidationError = TeacherSignUpInputValidationError.INITIAL,
)
