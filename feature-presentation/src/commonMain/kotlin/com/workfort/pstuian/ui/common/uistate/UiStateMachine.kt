package com.workfort.pstuian.ui.common.uistate

import kotlinx.coroutines.flow.StateFlow

interface UiStateMachine<UiState> {
    val uiState: StateFlow<UiState>
}
