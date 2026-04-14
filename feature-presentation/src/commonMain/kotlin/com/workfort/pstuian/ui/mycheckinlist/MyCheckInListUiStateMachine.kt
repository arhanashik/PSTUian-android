package com.workfort.pstuian.ui.mycheckinlist

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.ui.mycheckinlist.state.MyCheckInListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MyCheckInListUiStateMachine : UiStateMachine<MyCheckInListUiState> {

    private val _uiState = MutableStateFlow<MyCheckInListUiState>(MyCheckInListUiState.None)
    override val uiState: StateFlow<MyCheckInListUiState> = _uiState.asStateFlow()

    private fun updateUiState(
        updater: MyCheckInListUiState.() -> MyCheckInListUiState,
    ) = _uiState.update(updater)

    fun setInitialContent() = updateUiState {
        MyCheckInListUiState.Content()
    }

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is MyCheckInListUiState.None -> this
            is MyCheckInListUiState.Error -> this
            is MyCheckInListUiState.Content -> copy(isLoading = isLoading)
        }
    }

    fun showOperationLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is MyCheckInListUiState.None -> this
            is MyCheckInListUiState.Error -> this
            is MyCheckInListUiState.Content -> copy(isOperationLoading = isLoading)
        }
    }

    fun showContent(items: List<com.workfort.pstuian.featuredomain.model.CheckInEntity>) = updateUiState {
        when (this) {
            is MyCheckInListUiState.None -> MyCheckInListUiState.Content(items = items)
            is MyCheckInListUiState.Error -> MyCheckInListUiState.Content(items = items)
            is MyCheckInListUiState.Content -> copy(items = items, isLoading = false)
        }
    }

    fun showError(message: String) = updateUiState {
        MyCheckInListUiState.Error(message)
    }
}
