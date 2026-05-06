package com.workfort.pstuian.ui.donation.donors.state

import com.workfort.pstuian.featuredomain.model.Donor

sealed interface DonorsUiEvent {
    data object BackClicked : DonorsUiEvent
    data object Refresh : DonorsUiEvent
    data object DonateClicked : DonorsUiEvent
    data class DonorClicked(val donor: Donor) : DonorsUiEvent
}