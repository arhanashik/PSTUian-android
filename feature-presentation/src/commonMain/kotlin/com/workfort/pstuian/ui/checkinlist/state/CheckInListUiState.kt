package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity

sealed interface CheckInListUiState {
    data object None : CheckInListUiState
    data class Loading(val message: String) : CheckInListUiState
    data class Error(val message: String) : CheckInListUiState
    data class Content(
        val checkInLocation: CheckInLocationEntity? = null,
        val checkInList: List<CheckInEntity> = emptyList(),
        val isLoading: Boolean = false,
        val isOperationLoading: Boolean = false,
    ) : CheckInListUiState
}
