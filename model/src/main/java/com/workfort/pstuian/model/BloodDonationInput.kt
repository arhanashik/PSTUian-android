package com.workfort.pstuian.model

data class BloodDonationInput(
    val requestId: Int,
    val date: String,
    val message: String,
) {
    companion object {
        val INITIAL = BloodDonationInput(
            requestId = 0,
            date = "",
            message = "",
        )
    }
}

data class BloodDonationInputError(
    val requestId: String,
    val date: String,
    val message: String,
) {
    companion object {
        val INITIAL = BloodDonationInputError(
            requestId = "",
            date = "",
            message = "",
        )
    }

    fun isNotEmpty() = requestId.isNotEmpty() ||
            date.isNotEmpty() ||
            message.isNotEmpty()
}