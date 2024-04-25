package com.workfort.pstuian.app.ui.changepassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.ChangePasswordInput
import com.workfort.pstuian.model.ChangePasswordInputError
import com.workfort.pstuian.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChangePasswordViewModel(
    private val authRepo: AuthRepository,
    private val reducer: ChangePasswordScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<ChangePasswordScreenState> get() = _screenState

    private fun updateScreenState(update: ChangePasswordScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(ChangePasswordScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(ChangePasswordScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        ChangePasswordScreenStateUpdate.NavigateTo(
            ChangePasswordScreenState.NavigationState.GoBack,
        ),
    )

    fun onChangeInput(input: ChangePasswordInput) {
        inputCache = input
        validationErrorCache = input.validate()
        updateScreenState(
            ChangePasswordScreenStateUpdate.UpdateInput(inputCache, validationErrorCache)
        )
    }

    private var inputCache = ChangePasswordInput.INITIAL
    private var validationErrorCache = ChangePasswordInputError.INITIAL
    fun changePassword() {
        if (validationErrorCache.isNotEmpty()) return
        updateScreenState(
            ChangePasswordScreenStateUpdate.UpdateMessageState(
                ChangePasswordScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                authRepo.changePassword(
                    oldPassword = inputCache.oldPassword,
                    newPassword = inputCache.newPassword,
                )
            }.onSuccess {
                updateScreenState(
                    ChangePasswordScreenStateUpdate.UpdateMessageState(
                        ChangePasswordScreenState.DisplayState.MessageState.Success(
                            "Password changed successfully!",
                        ),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to change password. Please try again."
                updateScreenState(
                    ChangePasswordScreenStateUpdate.UpdateMessageState(
                        ChangePasswordScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun ChangePasswordInput.validate() = ChangePasswordInputError(
        oldPassword = if (oldPassword.isEmpty()) {
            "*Required"
        } else if (oldPassword.length < 4) {
            "*Too short"
        } else {
            ""
        },
        newPassword = if (newPassword.isEmpty()) {
            "*Required"
        } else if (newPassword.length < 4) {
            "*Too short"
        } else {
            ""
        },
        confirmPassword = if (confirmPassword.isEmpty()) {
            "*Required"
        } else if (confirmPassword.length < 4) {
            "*Too short"
        } else if (newPassword.isNotEmpty() && newPassword != confirmPassword) {
            "*Confirm password should be same as new password"
        } else {
            ""
        },
    )
}