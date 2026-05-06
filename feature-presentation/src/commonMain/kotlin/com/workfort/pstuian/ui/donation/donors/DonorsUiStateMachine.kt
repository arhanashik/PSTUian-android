package com.workfort.pstuian.ui.donation.donors

import com.workfort.pstuian.featuredomain.model.Donation
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonorsUiStateMachine : UiStateMachine<DonorsUiState> {
    private val _uiState = MutableStateFlow<DonorsUiState>(DonorsUiState.None)
    override val uiState: StateFlow<DonorsUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                DonorsUiState.None -> DonorsUiState.Content(isLoading = isLoading)
                is DonorsUiState.Content -> current.copy(isLoading = isLoading)
            }
        }
    }

    fun showDoners(donations: List<Donation>) {
        _uiState.update { current ->
            when (current) {
                DonorsUiState.None -> DonorsUiState.Content(donations = donations, isLoading = false)
                is DonorsUiState.Content -> current.copy(donations = donations, isLoading = false)
            }
        }
    }
}
