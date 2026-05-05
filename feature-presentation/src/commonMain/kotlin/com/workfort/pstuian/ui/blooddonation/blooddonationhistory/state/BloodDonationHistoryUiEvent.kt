package com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

sealed interface BloodDonationHistoryUiEvent {
    data object BackClicked : BloodDonationHistoryUiEvent
    data object CreateRequestClicked : BloodDonationHistoryUiEvent
    data class EditClicked(val item: BloodDonationEntity) : BloodDonationHistoryUiEvent
    data class DeleteClicked(val item: BloodDonationEntity) : BloodDonationHistoryUiEvent
    data object LoadMore : BloodDonationHistoryUiEvent
}
