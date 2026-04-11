package com.workfort.pstuian.model
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

    fun isNotEmpty(): Boolean = oldPassword.isNotEmpty() ||
            newPassword.isNotEmpty() ||
            confirmPassword.isNotEmpty()
}