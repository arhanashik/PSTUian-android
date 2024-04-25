package com.workfort.pstuian.app.ui.myblooddonationlist

import com.workfort.pstuian.model.BloodDonationEntity

sealed class MyBloodDonationListScreenUiEvent {
    data object None : MyBloodDonationListScreenUiEvent()
    data class OnLoadMoreData(val refresh: Boolean) : MyBloodDonationListScreenUiEvent()
    data object OnClickBack : MyBloodDonationListScreenUiEvent()
    data object OnClickCreateRequest : MyBloodDonationListScreenUiEvent()
    data class OnClickEdit(val item: BloodDonationEntity) : MyBloodDonationListScreenUiEvent()
    data class OnClickDelete(val item: BloodDonationEntity) : MyBloodDonationListScreenUiEvent()
    data class OnDelete(val item: BloodDonationEntity) : MyBloodDonationListScreenUiEvent()
    data object MessageConsumed : MyBloodDonationListScreenUiEvent()
    data object NavigationConsumed : MyBloodDonationListScreenUiEvent()
}