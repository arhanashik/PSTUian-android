package com.workfort.pstuian.model

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