package com.workfort.pstuian.app.ui.signin

import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.view.service.StateUpdate

sealed interface SignInScreenStateUpdate : StateUpdate<SignInScreenState> {

    data class ShowLoading(val isLoading: Boolean) : SignInScreenStateUpdate {
        override fun invoke(
            oldState: SignInScreenState,
        ): SignInScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class ChangeUserType(val userType: UserType) : SignInScreenStateUpdate {
        override fun invoke(
            oldState: SignInScreenState,
        ): SignInScreenState = with(oldState) {
            copy(displayState = displayState.copy(userType = userType))
        }
    }

    data class UpdateMessageState(
        val messageState: SignInScreenState.DisplayState.MessageState,
    ) : SignInScreenStateUpdate {
        override fun invoke(
            oldState: SignInScreenState
        ): SignInScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : SignInScreenStateUpdate {
        override fun invoke(
            oldState: SignInScreenState
        ): SignInScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: SignInScreenState.NavigationState,
    ) : SignInScreenStateUpdate {
        override fun invoke(
            oldState: SignInScreenState,
        ): SignInScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : SignInScreenStateUpdate {
        override fun invoke(
            oldState: SignInScreenState,
        ): SignInScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}