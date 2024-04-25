package com.workfort.pstuian.app.ui.donate

import com.workfort.pstuian.model.DonationInput

sealed class DonateScreenUiEvent {
    data object None : DonateScreenUiEvent()
    data object OnClickBack : DonateScreenUiEvent()
    data object OnClickSend : DonateScreenUiEvent()
    data class OnChangeInput(val input: DonationInput) : DonateScreenUiEvent()
    data object MessageConsumed : DonateScreenUiEvent()
    data object NavigationConsumed : DonateScreenUiEvent()
}