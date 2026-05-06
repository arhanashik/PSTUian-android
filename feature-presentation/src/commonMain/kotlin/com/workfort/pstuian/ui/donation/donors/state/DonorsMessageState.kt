package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donation

sealed interface DonorsMessageState {
    data class ShowDonorDetails(val donation: Donation) : DonorsMessageState
    data class Error(val message: String) : DonorsMessageState
}
