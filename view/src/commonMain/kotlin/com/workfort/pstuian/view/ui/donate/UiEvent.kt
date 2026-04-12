package com.workfort.pstuian.view.ui.donate

import com.workfort.pstuian.model.DonationInput

sealed class DonateUiEvent {
    data object OnClickBack : DonateUiEvent()
    data object OnClickSend : DonateUiEvent()
    data class OnChangeInput(val input: DonationInput) : DonateUiEvent()
    data object MessageConsumed : DonateUiEvent()
}