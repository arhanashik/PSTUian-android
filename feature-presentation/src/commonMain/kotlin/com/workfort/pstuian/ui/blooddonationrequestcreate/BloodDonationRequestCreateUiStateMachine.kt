package com.workfort.pstuian.ui.blooddonationrequestcreate

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInput
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestInputError
import com.workfort.pstuian.ui.blooddonationrequestcreate.state.BloodDonationRequestCreateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationRequestCreateUiStateMachine : UiStateMachine<BloodDonationRequestCreateUiState> {

    private val _state = MutableStateFlow<BloodDonationRequestCreateUiState>(BloodDonationRequestCreateUiState.None)
    override val uiState: StateFlow<BloodDonationRequestCreateUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: BloodDonationRequestCreateUiState.() -> BloodDonationRequestCreateUiState,
    ) = _state.update(updater)

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is BloodDonationRequestCreateUiState.None -> this
            is BloodDonationRequestCreateUiState.Content -> copy(isOperationLoading = isLoading)
        }
    }

    fun setInitialContent() = updateUiState {
        BloodDonationRequestCreateUiState.Content()
    }

    fun updateInput(input: BloodDonationRequestInput) = updateUiState {
        when (this) {
            is BloodDonationRequestCreateUiState.None -> this
            is BloodDonationRequestCreateUiState.Content -> copy(input = input)
        }
    }

    fun updateValidationError(validationError: BloodDonationRequestInputError) = updateUiState {
        when (this) {
            is BloodDonationRequestCreateUiState.None -> this
            is BloodDonationRequestCreateUiState.Content -> copy(validationError = validationError)
        }
    }
}
