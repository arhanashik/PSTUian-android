package com.workfort.pstuian.ui.faculty.batch.state

import com.workfort.pstuian.featuredomain.model.Batch

sealed interface BatchUiState {

    data object None : BatchUiState

    data object Loading : BatchUiState

    data class Content(
        val isLoading: Boolean = false,
        val batches: List<Batch> = emptyList(),
    ) : BatchUiState

    data class Error(val error: String? = null) : BatchUiState
}
