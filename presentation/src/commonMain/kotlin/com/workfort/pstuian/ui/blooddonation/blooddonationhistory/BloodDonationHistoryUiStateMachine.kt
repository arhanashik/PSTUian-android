package com.workfort.pstuian.ui.blooddonation.blooddonationhistory

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.blooddonation.blooddonationhistory.state.BloodDonationHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationHistoryUiStateMachine : UiStateMachine<BloodDonationHistoryUiState> {
    private val _uiState = MutableStateFlow<BloodDonationHistoryUiState>(BloodDonationHistoryUiState.None)
    override val uiState: StateFlow<BloodDonationHistoryUiState> = _uiState.asStateFlow()

    fun updateOperationLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is BloodDonationHistoryUiState.Content -> current.copy(isOperationLoading = isLoading)
                else -> BloodDonationHistoryUiState.Content(isOperationLoading = true)
            }
        }
    }

    fun updateContentLoading(isLoading: Boolean) {
        _uiState.update { current ->
            when (current) {
                is BloodDonationHistoryUiState.Content -> current.copy(isContentLoading = isLoading, error = null)
                else -> current
            }
        }
    }

    fun showDonations(donations: List<BloodDonationEntity>) {
        _uiState.update { current ->
            when (current) {
                is BloodDonationHistoryUiState.Content -> current.copy(
                    donations = donations,
                    isOperationLoading = false,
                    isContentLoading = false,
                    error = null,
                )
                else -> BloodDonationHistoryUiState.Content(
                    donations = donations,
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
                is BloodDonationHistoryUiState.Content -> current.copy(error = error)
                else -> BloodDonationHistoryUiState.Content(error = error)
            }
        }
    }
}
