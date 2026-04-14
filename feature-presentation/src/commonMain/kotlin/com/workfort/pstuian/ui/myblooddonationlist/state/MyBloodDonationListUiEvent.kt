package com.workfort.pstuian.ui.myblooddonationlist.state

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

sealed interface MyBloodDonationListUiEvent {
    data object OnClickBack : MyBloodDonationListUiEvent
    data object OnClickCreateRequest : MyBloodDonationListUiEvent
    data class OnClickEdit(val item: BloodDonationEntity) : MyBloodDonationListUiEvent
    data class OnClickDelete(val item: BloodDonationEntity) : MyBloodDonationListUiEvent
    data class OnConfirmDelete(val item: BloodDonationEntity) : MyBloodDonationListUiEvent
    data class OnLoadList(val refresh: Boolean) : MyBloodDonationListUiEvent
    data object MessageConsumed : MyBloodDonationListUiEvent
    data object NavigationConsumed : MyBloodDonationListUiEvent
}
