package com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.screendata.BloodDonationRequestDisplayData
import com.workfort.pstuian.ui.blooddonation.blooddonationrequestlist.state.BloodDonationRequestListUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationRequestListUiStateMachine : UiStateMachine<BloodDonationRequestListUiState> {

    private val _state = MutableStateFlow<BloodDonationRequestListUiState>(BloodDonationRequestListUiState.None)
    override val uiState: StateFlow<BloodDonationRequestListUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: BloodDonationRequestListUiState.() -> BloodDonationRequestListUiState,
    ) = _state.update(updater)

    fun setInitialContent() = updateUiState {
        BloodDonationRequestListUiState.Content()
    }

    fun updateRequestList(requestList: List<BloodDonationRequestDisplayData>) = updateUiState {
        when (this) {
            is BloodDonationRequestListUiState.None -> BloodDonationRequestListUiState.Content(
                requestList = requestList,
            )
            is BloodDonationRequestListUiState.Content -> copy(
                requestList = requestList,
                isLoading = false,
                error = null,
            )
        }
    }

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is BloodDonationRequestListUiState.None -> BloodDonationRequestListUiState.Content(isLoading = isLoading)
            is BloodDonationRequestListUiState.Content -> copy(isLoading = isLoading)
        }
    }

    fun updateLoadError(message: String) = updateUiState {
        when (this) {
            is BloodDonationRequestListUiState.None -> BloodDonationRequestListUiState.Content(
                error = message,
                isLoading = false,
            )
            is BloodDonationRequestListUiState.Content -> copy(
                error = message,
                isLoading = false,
            )
        }
    }
}
