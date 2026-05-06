package com.workfort.pstuian.ui.donation.donors

import com.workfort.pstuian.featuredomain.model.Donor
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.donation.donors.state.DonorsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DonorsUiStateMachine : UiStateMachine<DonorsUiState> {
    private val _uiState = MutableStateFlow<DonorsUiState>(DonorsUiState.None)
    override val uiState: StateFlow<DonorsUiState> = _uiState.asStateFlow()

    fun showLoading() {
        _uiState.update { DonorsUiState.Loading }
    }

    fun showContent(
        donorList: List<Donor>,
        isLoading: Boolean = false,
    ) {
        _uiState.update { DonorsUiState.Content(donorList, isLoading) }
    }
}
