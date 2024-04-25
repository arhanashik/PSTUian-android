package com.workfort.pstuian.app.ui.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ForgotPasswordViewModel (
    private val authRepo: AuthRepository,
    private val reducer: ForgotPasswordScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<ForgotPasswordScreenState> get() = _screenState

    private fun updateScreenState(update: ForgotPasswordScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(ForgotPasswordScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(ForgotPasswordScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        ForgotPasswordScreenStateUpdate.NavigateTo(
            ForgotPasswordScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickUserTypeBtn(userType: UserType) {
        updateScreenState(ForgotPasswordScreenStateUpdate.ChangeUserType(userType))
    }

    fun onClickSignIn() = updateScreenState(
        ForgotPasswordScreenStateUpdate.NavigateTo(
            ForgotPasswordScreenState.NavigationState.GoBack,
        ),
    )

    private fun getCurrentUserType() : UserType? {
        return _screenState.value.displayState.userType
    }

    fun sendPasswordResetLink(email: String) {
        val userType = getCurrentUserType() ?: return
        val validationError = if (email.isEmpty()) {
            "*Required"
        } else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            null
        }
        updateScreenState(ForgotPasswordScreenStateUpdate.SetValidationError(validationError))
        if (validationError != null) {
            return
        }
        viewModelScope.launch {
            updateScreenState(ForgotPasswordScreenStateUpdate.ShowLoading(true))
            runCatching {
                val response = authRepo.forgotPassword(userType.type, email)
                updateScreenState(ForgotPasswordScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    ForgotPasswordScreenStateUpdate.UpdateMessageState(
                        ForgotPasswordScreenState.DisplayState.MessageState
                            .PasswordResetLinkSentSuccess(message = response),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Couldn't send verification email!"
                updateScreenState(ForgotPasswordScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    ForgotPasswordScreenStateUpdate.UpdateMessageState(
                        ForgotPasswordScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}