package com.workfort.pstuian.ui.myblooddonationlist.state

import androidx.compose.runtime.Immutable
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

@Immutable
data class MyBloodDonationListUiState(
    val donations: List<BloodDonationEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isEndOfData: Boolean = false,
    val error: String? = null,
)
