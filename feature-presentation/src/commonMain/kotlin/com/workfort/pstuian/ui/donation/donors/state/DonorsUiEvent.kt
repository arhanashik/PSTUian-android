package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donation

sealed interface DonorsUiEvent {
    data object BackClicked : DonorsUiEvent
    data object DonateClicked : DonorsUiEvent
    data class DonorClicked(val donation: Donation) : DonorsUiEvent
    data object LoadMore : DonorsUiEvent
}