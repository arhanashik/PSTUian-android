package com.workfort.pstuian.app.ui.signup

import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.view.service.StateUpdate

sealed interface SignUpScreenStateUpdate : StateUpdate<SignUpScreenState> {

    data class ShowLoading(val isLoading: Boolean) : SignUpScreenStateUpdate {
        override fun invoke(
            oldState: SignUpScreenState,
        ): SignUpScreenState = with(oldState) {
            copy(displayState = displayState.copy(isLoading = isLoading))
        }
    }

    data class SetPanelToStudentSignUp(
        val signUpInput: StudentSignUpInput,
        val validationError: StudentSignUpInputValidationError,
    ) : SignUpScreenStateUpdate {
        override fun invoke(oldState: SignUpScreenState): SignUpScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    isLoading = false,
                    userType = UserType.STUDENT,
                    panelState = SignUpScreenState.DisplayState.PanelState.StudentSignUp(
                        signUpInput = signUpInput,
                        validationError = validationError,
                    ),
                ),
            )
        }
    }

    data class SetPanelToTeacherSignUp(
        val signUpInput: TeacherSignUpInput,
        val validationError: TeacherSignUpInputValidationError,
    ) : SignUpScreenStateUpdate {
        override fun invoke(oldState: SignUpScreenState): SignUpScreenState = with(oldState) {
            copy(
                displayState = displayState.copy(
                    isLoading = false,
                    userType = UserType.TEACHER,
                    panelState = SignUpScreenState.DisplayState.PanelState.TeacherSignUp(
                        signUpInput = signUpInput,
                        validationError = validationError,
                    )
                ),
            )
        }
    }

    data class UpdateMessageState(
        val messageState: SignUpScreenState.DisplayState.MessageState,
    ) : SignUpScreenStateUpdate {
        override fun invoke(
            oldState: SignUpScreenState
        ): SignUpScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = messageState))
        }
    }

    data object MessageConsumed : SignUpScreenStateUpdate {
        override fun invoke(
            oldState: SignUpScreenState
        ): SignUpScreenState = with(oldState) {
            copy(displayState = displayState.copy(messageState = null))
        }
    }

    data class NavigateTo(
        val newState: SignUpScreenState.NavigationState,
    ) : SignUpScreenStateUpdate {
        override fun invoke(
            oldState: SignUpScreenState,
        ): SignUpScreenState = with(oldState) {
            copy(navigationState = newState)
        }
    }

    data object NavigationConsumed : SignUpScreenStateUpdate {
        override fun invoke(
            oldState: SignUpScreenState,
        ): SignUpScreenState = with(oldState) {
            copy(navigationState = null)
        }
    }
}