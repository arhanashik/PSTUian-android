package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckInEntity

sealed interface MyCheckInListUiState {
    data object None : MyCheckInListUiState
    data class Error(val message: String) : MyCheckInListUiState
    data class Content(
        val items: List<CheckInEntity> = emptyList(),
        val isLoading: Boolean = false,
        val isOperationLoading: Boolean = false,
    ) : MyCheckInListUiState
}
