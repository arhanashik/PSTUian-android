package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donor

sealed interface DonorsMessageState {
    data class ShowDonorDetails(val donor: Donor) : DonorsMessageState
    data class Error(val message: String) : DonorsMessageState
}
