package com.workfort.pstuian.ui.changepassword.screendata

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordInput(
    val newPassword: String,
    val confirmPassword: String,
) {
    companion object {
        val INITIAL = ResetPasswordInput(
            newPassword = "",
            confirmPassword = "",
        )
    }
}

@Serializable
data class ResetPasswordInputError(
    val newPassword: String,
    val confirmPassword: String,
) {
    companion object {
        val INITIAL = ResetPasswordInputError(newPassword = "", confirmPassword = "")
    }

    fun hasError(): Boolean = newPassword.isNotEmpty() || confirmPassword.isNotEmpty()
}