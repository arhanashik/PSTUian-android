package com.workfort.pstuian.model

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