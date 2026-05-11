package com.workfort.pstuian.ui.deleteaccount.state

import androidx.compose.runtime.Immutable

@Immutable
data class DeleteAccountUiState(
    val input: String = "",
    val validationError: String = "",
)
