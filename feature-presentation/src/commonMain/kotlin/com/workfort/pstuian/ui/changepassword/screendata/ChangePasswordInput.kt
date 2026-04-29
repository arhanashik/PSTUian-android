package com.workfort.pstuian.ui.changepassword.screendata

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordInput(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String,
) {
    companion object {
        val INITIAL = ChangePasswordInput(
            oldPassword = "",
            newPassword = "",
            confirmPassword = "",
        )
    }
}

@Serializable
data class ChangePasswordInputError(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String,
) {
    companion object {
        val INITIAL = ChangePasswordInputError(
            oldPassword = "",
            newPassword = "",
            confirmPassword = "",
        )
    }

    fun hasError(): Boolean = oldPassword.isNotEmpty() ||
            newPassword.isNotEmpty() ||
            confirmPassword.isNotEmpty()
}