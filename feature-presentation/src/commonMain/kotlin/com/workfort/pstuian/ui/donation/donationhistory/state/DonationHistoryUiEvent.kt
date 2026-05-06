package com.workfort.pstuian.ui.donation.donationhistory.state

import com.workfort.pstuian.featuredomain.model.Donation

sealed interface DonationHistoryUiEvent {
    data object BackClicked : DonationHistoryUiEvent
    data object DonateClicked : DonationHistoryUiEvent
    data class DonorClicked(val donation: Donation) : DonationHistoryUiEvent
    data object LoadMore : DonationHistoryUiEvent
}