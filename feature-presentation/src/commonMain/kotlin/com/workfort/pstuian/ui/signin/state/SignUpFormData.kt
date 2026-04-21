package com.workfort.pstuian.ui.signin.state

data class SignInFormData(
    val email: String,
    val password: String,
)

data class SignUpFormData(
    val name: String,
    val email: String,
    val studentId: String,
    val registrationNumber: String,
    val faculty: String,
    val batch: String,
    val password: String,
)
