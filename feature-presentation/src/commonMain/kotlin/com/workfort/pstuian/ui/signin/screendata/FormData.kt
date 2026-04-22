package com.workfort.pstuian.ui.signin.screendata

import com.workfort.pstuian.util.isValidEmail

data class SignInFormData(
    val email: String,
    val password: String,
) {
    fun isInvalid(): Boolean = email.isEmpty() ||
            email.isValidEmail().not() ||
            password.isEmpty() ||
            password.length < 6
}

data class SignUpFormData(
    val name: String,
    val email: String,
    val studentId: String,
    val regNumber: String,
    val faculty: String,
    val batch: String,
    val password: String,
) {
    fun isInvalid(): Boolean = name.isEmpty() ||
            email.isEmpty() ||
            email.isValidEmail().not() ||
            studentId.isEmpty() ||
            regNumber.isEmpty() ||
            faculty.isEmpty() ||
            batch.isEmpty() ||
            password.isEmpty() ||
            password.length < 6
}
