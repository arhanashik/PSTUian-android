package com.workfort.pstuian.ui.contactus

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.ContactUsInput
import com.workfort.pstuian.featuredomain.model.ContactUsInputValidationError
import com.workfort.pstuian.ui.contactus.state.ContactUsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContactUsUiStateMachine : UiStateMachine<ContactUsUiState> {

    private val _state = MutableStateFlow<ContactUsUiState>(ContactUsUiState.None)
    override val uiState: StateFlow<ContactUsUiState> = _state.asStateFlow()

    private fun updateUiState(
        updater: ContactUsUiState.() -> ContactUsUiState,
    ) = _state.update(updater)

    fun setInitialContent() = updateUiState {
        ContactUsUiState.Content()
    }

    fun updateInput(input: ContactUsInput) = updateUiState {
        when (this) {
            is ContactUsUiState.Content -> copy(input = input)
            else -> this
        }
    }

    fun updateValidationError(validationError: ContactUsInputValidationError) = updateUiState {
        when (this) {
            is ContactUsUiState.Content -> copy(validationError = validationError)
            else -> this
        }
    }

    fun showLoading(isLoading: Boolean) = updateUiState {
        when (this) {
            is ContactUsUiState.Content -> copy(isLoading = isLoading)
            else -> this
        }
    }
}
