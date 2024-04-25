package com.workfort.pstuian.app.ui.deleteaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DeleteAccountViewModel(
    private val authRepo: AuthRepository,
    private val reducer: DeleteAccountScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<DeleteAccountScreenState> get() = _screenState

    private fun updateScreenState(update: DeleteAccountScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(DeleteAccountScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(DeleteAccountScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        DeleteAccountScreenStateUpdate.NavigateTo(
            DeleteAccountScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickDeleteAccountBtn() = updateScreenState(
        DeleteAccountScreenStateUpdate.UpdateMessageState(
            DeleteAccountScreenState.DisplayState.MessageState.ConfirmAccountDelete,
        ),
    )

    fun onChangeInput(input: String) {
        inputCache = input
        validationErrorCache = validate(input)
        updateScreenState(
            DeleteAccountScreenStateUpdate.UpdateInput(inputCache, validationErrorCache)
        )
    }

    fun onRequestRecovery() {
        messageConsumed()
        updateScreenState(
            DeleteAccountScreenStateUpdate.NavigateTo(
                DeleteAccountScreenState.NavigationState.ResetToContactUsScreen,
            ),
        )
    }

    fun onResetToHomeScreen() {
        messageConsumed()
        updateScreenState(
            DeleteAccountScreenStateUpdate.NavigateTo(
                DeleteAccountScreenState.NavigationState.ResetToHomeScreen,
            ),
        )
    }

    private var inputCache = ""
    private var validationErrorCache = ""
    fun deleteAccount() {
        if (validationErrorCache.isNotEmpty()) return
        updateScreenState(
            DeleteAccountScreenStateUpdate.UpdateMessageState(
                DeleteAccountScreenState.DisplayState.MessageState.Loading(cancelable = false),
            ),
        )
        viewModelScope.launch {
            runCatching {
                authRepo.deleteAccount(password = inputCache)
            }.onSuccess {
                updateScreenState(
                    DeleteAccountScreenStateUpdate.UpdateMessageState(
                        DeleteAccountScreenState.DisplayState.MessageState.Success(
                            "Account deleted successfully!",
                        ),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to delete account. Please try again."
                updateScreenState(
                    DeleteAccountScreenStateUpdate.UpdateMessageState(
                        DeleteAccountScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }

    private fun validate(password: String): String {
        return if (password.isEmpty()) {
            "*Required"
        } else if (password.length < 4) {
            "*Too short"
        } else {
            ""
        }
    }
}