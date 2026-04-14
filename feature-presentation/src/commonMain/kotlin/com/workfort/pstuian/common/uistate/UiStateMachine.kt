package com.workfort.pstuian.common.uistate

import kotlinx.coroutines.flow.StateFlow

interface UiStateMachine<UiState> {
    val uiState: StateFlow<UiState>
}
