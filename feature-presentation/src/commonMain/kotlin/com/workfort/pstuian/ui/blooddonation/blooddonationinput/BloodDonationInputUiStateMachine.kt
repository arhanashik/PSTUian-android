package com.workfort.pstuian.ui.blooddonation.blooddonationinput

import com.workfort.pstuian.ui.blooddonation.blooddonationinput.state.BloodDonationInputData
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

    fun setInitialContent(title: String, requestId: Int) = updateUiState {
        BloodDonationInputUiState.Content(title = title, inputData = BloodDonationInputData(requestId = requestId))
    }

    fun updateInputData(inputData: BloodDonationInputData) = updateUiState {
        when (this) {
            is BloodDonationInputUiState.None -> this
            is BloodDonationInputUiState.Content -> copy(
                inputData = inputData,
                enableSendButton = !inputData.hasError(),
                isOperationLoading = false,
            )
        }
    }
}
