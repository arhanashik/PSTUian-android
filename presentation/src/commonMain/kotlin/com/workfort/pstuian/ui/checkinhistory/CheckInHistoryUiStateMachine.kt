package com.workfort.pstuian.ui.checkinhistory

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.checkinhistory.state.CheckInHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CheckInHistoryUiStateMachine : UiStateMachine<CheckInHistoryUiState> {
    private val _uiState = MutableStateFlow<CheckInHistoryUiState>(CheckInHistoryUiState.None)
    override val uiState: StateFlow<CheckInHistoryUiState> = _uiState.asStateFlow()

    fun updateOperationLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is CheckInHistoryUiState.Content -> current.copy(isOperationLoading = isLoading)
                else -> CheckInHistoryUiState.Content(isOperationLoading = true)
            }
        }
    }

    fun updateContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is CheckInHistoryUiState.Content -> current.copy(isContentLoading = isLoading, error = null)
                else -> current
            }
        }
    }

    fun showCheckIns(checkIns: List<CheckIn>) {
        _uiState.update { current ->
            when (current) {
                is CheckInHistoryUiState.Content -> current.copy(
                    checkIns = checkIns,
                    isOperationLoading = false,
                    isContentLoading = false,
                    error = null,
                )
                else -> CheckInHistoryUiState.Content(
                    checkIns = checkIns,
                    isOperationLoading = false,
                    isContentLoading = false,
                    error = null,
                )
            }
        }
    }

    fun showError(error: String) {
        _uiState.update { current ->
            when (current) {
                is CheckInHistoryUiState.Content -> current.copy(error = error)
                else -> CheckInHistoryUiState.Content(error = error)
            }
        }
    }
}
