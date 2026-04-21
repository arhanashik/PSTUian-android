package com.workfort.pstuian.ui.signin.state

/**
 * Values submitted from the sign-up panel of the unified auth screen.
 */
data class SignUpFormData(
    val name: String,
    val email: String,
    val studentId: String,
    val registrationNumber: String,
    val faculty: String,
    val batch: String,
    val password: String,
)
