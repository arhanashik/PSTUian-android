package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class DonationInput(
    val name: String,
    val email: String,
    val reference: String,
    val message: String,
) {
    companion object {
        val INITIAL = DonationInput(
            name = "",
            email = "",
            reference = "",
            message = "",
        )
    }
}

@Serializable
data class DonationInputValidationError(
    val name: String,
    val email: String,
    val reference: String,
    val message: String,
) {
    companion object {
        val INITIAL = DonationInputValidationError(
            name = "",
            email = "",
            reference = "",
            message = "",
        )
    }

    fun isNotEmpty() = name.isNotEmpty() ||
            email.isNotEmpty() ||
            reference.isNotEmpty() ||
            message.isNotEmpty()
}