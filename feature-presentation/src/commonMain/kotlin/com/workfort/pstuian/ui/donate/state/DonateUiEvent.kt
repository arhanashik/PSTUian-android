package com.workfort.pstuian.ui.donate.state

import com.workfort.pstuian.featuredomain.model.DonationInput

sealed interface DonateUiEvent {
    data object BackClicked : DonateUiEvent
    data class ChangeInput(val input: DonationInput) : DonateUiEvent
    data object SendDonationInfo : DonateUiEvent
}
