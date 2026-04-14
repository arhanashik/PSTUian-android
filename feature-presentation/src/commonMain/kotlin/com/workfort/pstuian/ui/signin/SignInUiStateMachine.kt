package com.workfort.pstuian.ui.signin

import com.workfort.pstuian.common.uistate.UiStateMachine
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.ui.signin.state.MessageState
import com.workfort.pstuian.ui.signin.state.NavigationState
import com.workfort.pstuian.ui.signin.state.SignInUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInUiStateMachine : UiStateMachine<SignInUiState> {
    private val _uiState = MutableStateFlow(SignInUiState())
    override val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun showLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateUserType(userType: UserType) {
        _uiState.update { it.copy(userType = userType) }
    }

    fun showMessage(messageState: MessageState?) {
        _uiState.update { it.copy(messageState = messageState) }
    }

    fun navigateTo(navigationState: NavigationState?) {
        _uiState.update { it.copy(navigationState = navigationState) }
    }
}
