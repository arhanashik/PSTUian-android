package com.workfort.pstuian.ui.checkinlist

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.state.CheckInListUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CheckInListUiStateMachine : UiStateMachine<CheckInListUiState> {

    private val _state = MutableStateFlow<CheckInListUiState>(CheckInListUiState.None)
    override val uiState: StateFlow<CheckInListUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: CheckInListUiState.() -> CheckInListUiState,
    ) = _state.update(updater)

    fun showLoading(message: String = "") = updateUiState {
        CheckInListUiState.Loading(message)
    }

    fun showError(message: String) = updateUiState {
        CheckInListUiState.Error(message)
    }

    fun setInitialContent() = updateUiState {
        CheckInListUiState.Content()
    }

    fun showCheckInListLocation(checkInLocation: CheckInLocation?) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(checkInLocation = checkInLocation)
            else -> CheckInListUiState.Content(checkInLocation = checkInLocation)
        }
    }

    fun showCheckInList(checkInList: List<CheckIn>, isLoading: Boolean) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(
                checkInList = checkInList,
                isLoading = isLoading,
            )
            else -> CheckInListUiState.Content(
                checkInList = checkInList,
                isLoading = isLoading,
            )
        }
    }

    fun showOperationLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(isOperationLoading = isLoading)
            else -> this
        }
    }
}
