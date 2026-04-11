package com.workfort.pstuian.model
import kotlinx.serialization.Serializable






@Serializable
data class ContactUsInput(
    val name: String,
    val email: String,
    val message: String,
) {
    companion object {
        val INITIAL = ContactUsInput(
            name = "",
            email = "",
            message = "",
        )
    }
}

@Serializable
data class ContactUsInputValidationError(
    val name: String,
    val email: String,
    val message: String,
) {
    companion object {
        val INITIAL = ContactUsInputValidationError(
            name = "",
            email = "",
            message = "",
        )
    }

    fun isNotEmpty() = name.isNotEmpty() || email.isNotEmpty() || message.isNotEmpty()
}