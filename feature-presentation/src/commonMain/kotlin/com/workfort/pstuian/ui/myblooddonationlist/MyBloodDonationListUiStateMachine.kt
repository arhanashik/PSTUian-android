package com.workfort.pstuian.ui.myblooddonationlist

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.ui.myblooddonationlist.state.MyBloodDonationListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyBloodDonationListUiStateMachine : UiStateMachine<MyBloodDonationListUiState> {
    private val _uiState = MutableStateFlow(MyBloodDonationListUiState())
    override val uiState: StateFlow<MyBloodDonationListUiState> = _uiState.asStateFlow()

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
