package com.workfort.pstuian.ui.donation.donationhistory.state

import com.workfort.pstuian.featuredomain.model.Donation

sealed interface DonationHistoryMessageState {
    data class ShowDonorDetails(val donation: Donation) : DonationHistoryMessageState
    data class Error(val message: String) : DonationHistoryMessageState
}
