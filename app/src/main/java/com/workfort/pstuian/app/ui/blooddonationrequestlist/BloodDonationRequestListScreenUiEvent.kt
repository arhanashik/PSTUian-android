package com.workfort.pstuian.app.ui.blooddonationrequestlist

import com.workfort.pstuian.model.BloodDonationRequestEntity

sealed class BloodDonationRequestListScreenUiEvent {
    data object None : BloodDonationRequestListScreenUiEvent()
    data class OnLoadMoreData(val refresh: Boolean) : BloodDonationRequestListScreenUiEvent()
    data object OnClickBack : BloodDonationRequestListScreenUiEvent()
    data class OnClickItem(
        val item: BloodDonationRequestEntity,
    ) : BloodDonationRequestListScreenUiEvent()
    data class OnClickCall(val phoneNumber: String) : BloodDonationRequestListScreenUiEvent()
    data object OnClickCreateRequest : BloodDonationRequestListScreenUiEvent()
    data class OnCall(val phoneNumber: String) : BloodDonationRequestListScreenUiEvent()
    data object MessageConsumed : BloodDonationRequestListScreenUiEvent()
    data object NavigationConsumed : BloodDonationRequestListScreenUiEvent()
}