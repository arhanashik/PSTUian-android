package com.workfort.pstuian.ui.donors

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.DonorEntity
import com.workfort.pstuian.ui.donors.state.DonorsUiState
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

    fun showContent(donorList: List<DonorEntity>) {
        _uiState.update { DonorsUiState.Content(donorList) }
    }
}
