package com.workfort.pstuian.ui.blooddonation.blooddonationinput

import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputUiState
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationInputUiStateMachine : UiStateMachine<BloodDonationInputUiState> {

    private val _state = MutableStateFlow<BloodDonationInputUiState>(BloodDonationInputUiState.None())
    override val uiState: StateFlow<BloodDonationInputUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: BloodDonationInputUiState.() -> BloodDonationInputUiState,
    ) = _state.update(updater)

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is BloodDonationInputUiState.None -> this
            is BloodDonationInputUiState.Content -> copy(isOperationLoading = isLoading)
        }
    }

    fun setInitialContent(title: String) = updateUiState {
        BloodDonationInputUiState.Content(title = title)
    }

    fun updateRequestId(requestId: Int) = updateUiState {
        when (this) {
            is BloodDonationInputUiState.None -> this
            is BloodDonationInputUiState.Content -> copy(
                inputData = inputData.copy(requestId = requestId),
            )
        }
    }

    fun updateDate(date: Long?, formattedDate: String) = updateUiState {
        when (this) {
            is BloodDonationInputUiState.None -> this
            is BloodDonationInputUiState.Content -> copy(
                inputData = inputData.copy(date = date, formattedDate = formattedDate),
                enableSendButton = formattedDate.isNotEmpty(),
            )
        }
    }

    fun updateInfo(info: String) = updateUiState {
        when (this) {
            is BloodDonationInputUiState.None -> this
            is BloodDonationInputUiState.Content -> copy(
                inputData = inputData.copy(info = info),
            )
        }
    }
}
