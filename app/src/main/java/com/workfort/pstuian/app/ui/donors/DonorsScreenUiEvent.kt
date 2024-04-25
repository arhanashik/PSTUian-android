package com.workfort.pstuian.app.ui.donors

import com.workfort.pstuian.model.DonorEntity

sealed class DonorsScreenUiEvent {
    data object None : DonorsScreenUiEvent()
    data object OnLoadData : DonorsScreenUiEvent()
    data object OnClickBack : DonorsScreenUiEvent()
    data class OnClickItem(val item: DonorEntity) : DonorsScreenUiEvent()
    data object OnClickDonate : DonorsScreenUiEvent()
    data object MessageConsumed : DonorsScreenUiEvent()
    data object NavigationConsumed : DonorsScreenUiEvent()
}