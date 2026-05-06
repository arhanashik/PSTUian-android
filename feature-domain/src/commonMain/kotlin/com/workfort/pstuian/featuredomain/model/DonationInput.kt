package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class DonationInput(
    val name: String,
    val email: String,
    val reference: String,
    val amount: String,
    val message: String,
) {
    companion object {
        val INITIAL = DonationInput(
            name = "",
            email = "",
            reference = "",
            amount = "",
            message = "",
        )
    }
}

@Serializable
data class DonationInputValidationError(
    val name: String,
    val email: String,
    val reference: String,
    val amount: String,
    val message: String,
) {
    companion object {
        val INITIAL = DonationInputValidationError(
            name = "",
            email = "",
            reference = "",
            amount = "",
            message = "",
        )
    }

    fun isNotEmpty() = name.isNotEmpty() ||
            email.isNotEmpty() ||
            reference.isNotEmpty() ||
            amount.isNotEmpty() ||
            message.isNotEmpty()
}