package com.workfort.pstuian.ui.mycheckinlist

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.ui.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCheckInListUiStateMachine : UiStateMachine<MyCheckInListUiState> {
    private val _uiState = MutableStateFlow(MyCheckInListUiState())
    override val uiState: StateFlow<MyCheckInListUiState> = _uiState.asStateFlow()

    fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateOperationLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isOperationLoading = isLoading) }
    }

    fun updateData(items: List<CheckIn>) {
        _uiState.update {
            it.copy(
                items = items,
                isLoading = false,
                error = null
            )
        }
    }

    fun updateError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = message
            )
        }
    }
}
