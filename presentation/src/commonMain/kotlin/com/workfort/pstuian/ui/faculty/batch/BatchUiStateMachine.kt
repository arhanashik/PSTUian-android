package com.workfort.pstuian.ui.faculty.batch

import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.faculty.batch.state.BatchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BatchUiStateMachine : UiStateMachine<BatchUiState> {

    private val _uiState = MutableStateFlow<BatchUiState>(BatchUiState.None)
    override val uiState: StateFlow<BatchUiState> = _uiState.asStateFlow()

    fun showInitialContent() {
        _uiState.update { BatchUiState.Content() }
    }

    fun showOperationLoading() {
        _uiState.update { BatchUiState.Loading }
    }

    fun showContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is BatchUiState.Content -> current.copy(isLoading = isLoading)
                else -> current
            }
        }
    }

    fun showBatches(batches: List<Batch>) {
        _uiState.update { current ->
            when (current) {
                is BatchUiState.Content -> current.copy(batches = batches, isLoading = false)
                else -> BatchUiState.Content(batches = batches, isLoading = false)
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { BatchUiState.Error(error) }
    }
}
