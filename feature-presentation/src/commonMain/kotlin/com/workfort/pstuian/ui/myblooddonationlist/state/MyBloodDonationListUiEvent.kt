package com.workfort.pstuian.ui.myblooddonationlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

sealed interface MyBloodDonationListUiEvent {
    data object BackClicked : MyBloodDonationListUiEvent
    data object CreateRequestClicked : MyBloodDonationListUiEvent
    data class EditClicked(val item: BloodDonationEntity) : MyBloodDonationListUiEvent
    data class DeleteClicked(val item: BloodDonationEntity) : MyBloodDonationListUiEvent
    data class ConfirmDeleteClicked(val item: BloodDonationEntity) : MyBloodDonationListUiEvent
    data class LoadList(val refresh: Boolean) : MyBloodDonationListUiEvent
}
