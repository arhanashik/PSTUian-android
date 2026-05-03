package com.workfort.pstuian.ui.mycheckinlist.state

import com.workfort.pstuian.featuredomain.model.CheckIn

sealed interface MyCheckInListUiState {

    data object None : MyCheckInListUiState

    data class Content(
        val isOperationLoading: Boolean = false,
        val checkIns: List<CheckIn> = emptyList(),
        val isContentLoading: Boolean = false,
        val error: String? = null,
    ) : MyCheckInListUiState
}
