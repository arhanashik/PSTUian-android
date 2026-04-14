package com.workfort.pstuian.ui.donors.state

import com.workfort.pstuian.featuredomain.model.DonorEntity

sealed interface DonorsUiState {
    data object None : DonorsUiState
    data object Loading : DonorsUiState
    data class Content(
        val donorList: List<DonorEntity>,
        val showLoadingMore: Boolean = false,
    ) : DonorsUiState
}