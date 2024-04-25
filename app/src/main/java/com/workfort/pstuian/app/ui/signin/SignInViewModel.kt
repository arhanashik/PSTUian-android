package com.workfort.pstuian.app.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.util.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SignInViewModel (
    private val authRepo: AuthRepository,
    private val reducer: SignInScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(reducer.initial)
    val screenState: StateFlow<SignInScreenState> get() = _screenState

    private fun updateScreenState(update: SignInScreenStateUpdate) =
        _screenState.update { oldState -> reducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(SignInScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(SignInScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        SignInScreenStateUpdate.NavigateTo(
            SignInScreenState.NavigationState.GoBack(isSignedIn = false),
        ),
    )

    fun onClickUserTypeBtn(userType: UserType) {
        updateScreenState(SignInScreenStateUpdate.ChangeUserType(userType))
    }

    fun onClickForgotPassword() = updateScreenState(
        SignInScreenStateUpdate.NavigateTo(
            SignInScreenState.NavigationState.GoToForgotPasswordScreen,
        ),
    )

    fun onClickSignUp() = updateScreenState(
        SignInScreenStateUpdate.NavigateTo(
            SignInScreenState.NavigationState.GoToSignUpScreen,
        ),
    )

    fun onClickEmailVerification() = updateScreenState(
        SignInScreenStateUpdate.NavigateTo(
            SignInScreenState.NavigationState.GoToEmailVerificationScreen,
        ),
    )

    private fun getCurrentUserType() : UserType? {
        return _screenState.value.displayState.userType
    }

    fun signIn(email: String, password: String) {
        val userType = getCurrentUserType() ?: return
        if (
            email.isEmpty() ||
            email.isValidEmail().not() ||
            password.isEmpty() ||
            password.length < 4
        ) {
            updateScreenState(
                SignInScreenStateUpdate.UpdateMessageState(
                    SignInScreenState.DisplayState.MessageState.Error(
                        "Please enter valid credentials and try again "
                    ),
                ),
            )
            return
        }
        viewModelScope.launch {
            updateScreenState(SignInScreenStateUpdate.ShowLoading(true))
            runCatching {
                authRepo.signIn(email, password, userType.type)
                updateScreenState(SignInScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    SignInScreenStateUpdate.UpdateMessageState(
                        SignInScreenState.DisplayState.MessageState.Success(
                            message = "Signed in successfully!",
                            showToast = true,
                        ),
                    ),
                )
                updateScreenState(
                    SignInScreenStateUpdate.NavigateTo(
                        SignInScreenState.NavigationState.GoBack(isSignedIn = true),
                    ),
                )
            }.onFailure {
                val msg = it.message?: "Failed to Sign in. Please try again."
                updateScreenState(SignInScreenStateUpdate.ShowLoading(false))
                updateScreenState(
                    SignInScreenStateUpdate.UpdateMessageState(
                        SignInScreenState.DisplayState.MessageState.Error(msg)
                    ),
                )
            }
        }
    }
}