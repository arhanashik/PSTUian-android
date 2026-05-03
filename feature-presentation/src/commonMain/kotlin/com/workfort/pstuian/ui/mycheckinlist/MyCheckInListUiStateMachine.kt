package com.workfort.pstuian.ui.mycheckinlist

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCheckInListUiStateMachine : UiStateMachine<MyCheckInListUiState> {
    private val _uiState = MutableStateFlow<MyCheckInListUiState>(MyCheckInListUiState.None)
    override val uiState: StateFlow<MyCheckInListUiState> = _uiState.asStateFlow()

    fun updateOperationLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is MyCheckInListUiState.Content -> current.copy(isOperationLoading = isLoading)
                else -> MyCheckInListUiState.Content(isOperationLoading = true)
            }
        }
    }

    fun updateContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is MyCheckInListUiState.Content -> current.copy(isContentLoading = isLoading, error = null)
                else -> current
            }
        }
    }

    fun showCheckIns(checkIns: List<CheckIn>) {
        _uiState.update { current ->
            when (current) {
                is MyCheckInListUiState.Content -> current.copy(
                    checkIns = checkIns,
                    isOperationLoading = false,
                    isContentLoading = false,
                    error = null,
                )
                else -> MyCheckInListUiState.Content(
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
                is MyCheckInListUiState.Content -> current.copy(error = error)
                else -> MyCheckInListUiState.Content(error = error)
            }
        }
    }
}
