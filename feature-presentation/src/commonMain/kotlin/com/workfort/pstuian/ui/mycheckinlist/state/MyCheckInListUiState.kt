package com.workfort.pstuian.ui.mycheckinlist.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.CheckIn

@Immutable
data class MyCheckInListUiState(
    val items: List<CheckIn> = emptyList(),
    val isLoading: Boolean = false,
    val isOperationLoading: Boolean = false,
    val error: String? = null,
)
