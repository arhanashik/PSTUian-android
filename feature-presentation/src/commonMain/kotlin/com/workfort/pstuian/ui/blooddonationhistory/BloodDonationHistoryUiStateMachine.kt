package com.workfort.pstuian.ui.blooddonationhistory

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.blooddonationhistory.state.BloodDonationHistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BloodDonationHistoryUiStateMachine : UiStateMachine<BloodDonationHistoryUiState> {
    private val _uiState = MutableStateFlow(BloodDonationHistoryUiState())
    override val uiState: StateFlow<BloodDonationHistoryUiState> = _uiState.asStateFlow()

    fun updateLoading(isLoading: Boolean, isRefresh: Boolean) {
        _uiState.update {
            it.copy(
                donations = if (isRefresh) emptyList() else it.donations,
                isLoading = isLoading,
                error = if (isLoading && isRefresh) null else it.error
            )
        }
    }

    fun updateData(donations: List<BloodDonationEntity>, isEndOfData: Boolean) {
        _uiState.update {
            it.copy(
                donations = donations,
                isLoading = false,
                isEndOfData = isEndOfData,
                error = null,
            )
        }
    }

    fun updateError(message: String, isFirstPage: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isEndOfData = true,
                error = if (isFirstPage) message else null
            )
        }
    }
}
