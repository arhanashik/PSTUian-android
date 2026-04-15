package com.workfort.pstuian.ui.mycheckinlist.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.CheckInEntity

@Immutable
data class MyCheckInListUiState(
    val items: List<CheckInEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isOperationLoading: Boolean = false,
    val error: String? = null,
)
