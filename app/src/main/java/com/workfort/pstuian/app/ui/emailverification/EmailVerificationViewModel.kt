package com.workfort.pstuian.app.ui.emailverification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class EmailVerificationViewModel (
    private val authRepo: AuthRepository,
    private val reducer: EmailVerificationScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<EmailVerificationScreenState> get() = _screenState

    private fun updateScreenState(update: EmailVerificationScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(EmailVerificationScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(EmailVerificationScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        EmailVerificationScreenStateUpdate.NavigateTo(
            EmailVerificationScreenState.NavigationState.GoBack,
        ),
    )

    fun onClickUserTypeBtn(userType: UserType) {
        updateScreenState(EmailVerificationScreenStateUpdate.ChangeUserType(userType))
    }

    fun onClickSignIn() = updateScreenState(
        EmailVerificationScreenStateUpdate.NavigateTo(
            EmailVerificationScreenState.NavigationState.GoBack,
        ),
    )

    private fun getCurrentUserType() : UserType? {
        return _screenState.value.displayState.userType
    }

    fun sendVerificationEmail(email: String) {
        val userType = getCurrentUserType() ?: return
        val validationError = if (email.isEmpty()) {
            "*Required"
        } else if (email.isValidEmail().not()) {
            "*Invalid email address"
        } else {
            null
        }
        updateScreenState(EmailVerificationScreenStateUpdate.SetValidationError(validationError))
        if (validationError != null) {
            return
        }
        viewModelScope.launch {
            updateScreenState(EmailVerificationScreenStateUpdate.ShowLoading(true))
            runCatching {
                val response = authRepo.emailVerification(userType.type, email)
                updateScreenState(EmailVerificationScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    EmailVerificationScreenStateUpdate.UpdateMessageState(
                        EmailVerificationScreenState.DisplayState.MessageState.EmailSentSuccess(
                            message = response,
                        ),
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Couldn't send verification email!"
                updateScreenState(EmailVerificationScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    EmailVerificationScreenStateUpdate.UpdateMessageState(
                        EmailVerificationScreenState.DisplayState.MessageState.Error(message),
                    ),
                )
            }
        }
    }
}