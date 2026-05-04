package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest


data class BloodDonationRequestDisplayData(
    val bloodDonationRequest: BloodDonationRequest,
    val contacts: List<String>,
    val needBeforeFormattedDate: String,
    val isOwnItem: Boolean,
)