package com.workfort.pstuian.ui.checkinlist

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData
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

    fun showOperationLoading() = updateUiState {
        CheckInListUiState.Loading
    }

    fun showLocationListLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(isLocationListLoading = isLoading)
            else -> this
        }
    }

    fun showCheckInListLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(isCheckInListLoading = isLoading)
            else -> this
        }
    }

    fun showInitialContent(
        checkInLocations: List<CheckInLocation>,
        selectedCheckInLocationId: Int,
    ) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(
                checkInLocations = checkInLocations,
                selectedCheckInLocationId = selectedCheckInLocationId,
            )
            else -> CheckInListUiState.Content(
                checkInLocations = checkInLocations,
                selectedCheckInLocationId = selectedCheckInLocationId,
            )
        }
    }

    fun updatedSelectedCheckInLocationId(selectedCheckInLocationId: Int) = updateUiState {
        when (this) {
            is CheckInListUiState.Content -> copy(selectedCheckInLocationId = selectedCheckInLocationId)
            else -> this
        }
    }

    fun showCheckInList(currentUserCheckIn: CheckInDisplayData?, otherCheckIns: List<CheckInDisplayData>) {
        updateUiState {
            when (this) {
                is CheckInListUiState.Content -> copy(
                    currentUserCheckIn = currentUserCheckIn,
                    otherCheckIns = otherCheckIns,
                    isCheckInListLoading = false,
                )
                else -> this
            }
        }
    }

    fun showError(message: String) = updateUiState {
        CheckInListUiState.Error(message)
    }
}
