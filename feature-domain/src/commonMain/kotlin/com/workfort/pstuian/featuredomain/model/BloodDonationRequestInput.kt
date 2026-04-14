package com.workfort.pstuian.featuredomain.model
import kotlinx.serialization.Serializable

@Serializable
data class BloodDonationRequestInput(
    val bloodGroup: String,
    val date: String,
    val contact: String,
    val message: String,
) {
    companion object {
        val INITIAL = BloodDonationRequestInput(
            bloodGroup = "",
            date = "",
            contact = "",
            message = "",
        )
    }
}

@Serializable
data class BloodDonationRequestInputError(
    val bloodGroup: String,
    val date: String,
    val contact: String,
    val message: String,
) {
    companion object {
        val INITIAL = BloodDonationRequestInputError(
            bloodGroup = "",
            date = "",
            contact = "",
            message = "",
        )
    }

    fun isNotEmpty() = bloodGroup.isNotEmpty() ||
            date.isNotEmpty() ||
            contact.isNotEmpty() ||
            message.isNotEmpty()
}