package com.workfort.pstuian.ui.myblooddonationlist.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

@Immutable
data class MyBloodDonationListUiState(
    val donations: List<BloodDonationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isEndOfData: Boolean = false,
    val error: String? = null,
    val messageState: MessageState? = null,
    val navigationState: NavigationState? = null,
) {
    sealed interface MessageState {
        data class Loading(val cancelable: Boolean) : MessageState
        data class ConfirmDelete(val item: BloodDonationEntity) : MessageState
        data class Success(val message: String) : MessageState
        data class Error(val message: String) : MessageState
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data object BloodDonationRequestCreateScreen : NavigationState
        data class BloodDonationRequestEditScreen(val item: BloodDonationEntity) : NavigationState
    }
}
