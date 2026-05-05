package com.workfort.pstuian.ui.checkin

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.ui.checkin.displaydata.CheckInDisplayData
import com.workfort.pstuian.ui.checkin.state.CheckInUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CheckInUiStateMachine : UiStateMachine<CheckInUiState> {

    private val _state = MutableStateFlow<CheckInUiState>(CheckInUiState.None)
    override val uiState: StateFlow<CheckInUiState> = _state.asStateFlow()

    private fun updateUiState(updater: CheckInUiState.() -> CheckInUiState) = _state.update(updater)

    fun showOperationLoading() = updateUiState {
        CheckInUiState.Loading
    }

    fun showLocationListLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is CheckInUiState.Content -> copy(isLocationListLoading = isLoading)
            else -> this
        }
    }

    fun showCheckInLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is CheckInUiState.Content -> copy(isCheckInLoading = isLoading)
            else -> this
        }
    }

    fun showInitialContent(
        checkInLocations: List<CheckInLocation>,
        selectedLocationId: Int,
    ) = updateUiState {
        when (this) {
            is CheckInUiState.Content -> copy(
                checkInLocations = checkInLocations,
                selectedLocationId = selectedLocationId,
            )
            else -> CheckInUiState.Content(
                checkInLocations = checkInLocations,
                selectedLocationId = selectedLocationId,
            )
        }
    }

    fun updatedSelectedCheckInLocationId(selectedCheckInLocationId: Int) = updateUiState {
        when (this) {
            is CheckInUiState.Content -> copy(selectedLocationId = selectedCheckInLocationId)
            else -> this
        }
    }

    fun showCheckIn(currentUserCheckIn: CheckInDisplayData?, otherCheckIns: List<CheckInDisplayData>) {
        updateUiState {
            when (this) {
                is CheckInUiState.Content -> copy(
                    currentUserCheckIn = currentUserCheckIn,
                    otherCheckIns = otherCheckIns,
                    isCheckInLoading = false,
                )
                else -> this
            }
        }
    }

    fun showError(message: String) = updateUiState {
        CheckInUiState.Error(message)
    }
}
