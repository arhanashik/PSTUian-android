package com.workfort.pstuian.ui.donors.state

import com.workfort.pstuian.featuredomain.model.DonorEntity

sealed interface DonorsUiEvent {
    data object BackClicked : DonorsUiEvent
    data object Refresh : DonorsUiEvent
    data object DonateClicked : DonorsUiEvent
    data class DonorClicked(val donor: DonorEntity) : DonorsUiEvent
}