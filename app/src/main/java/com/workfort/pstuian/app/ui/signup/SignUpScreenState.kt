package com.workfort.pstuian.app.ui.signup

import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.StudentSignUpInput
import com.workfort.pstuian.model.StudentSignUpInputValidationError
import com.workfort.pstuian.model.TeacherSignUpInput
import com.workfort.pstuian.model.TeacherSignUpInputValidationError
import com.workfort.pstuian.model.UserType


data class SignUpScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val isLoading: Boolean,
        val userType: UserType?,
        val panelState: PanelState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                isLoading = false,
                userType = null,
                panelState = PanelState.None,
                messageState = null,
            )
        }
        sealed interface PanelState {
            data object None : PanelState
            data class StudentSignUp(
                val signUpInput: StudentSignUpInput,
                val validationError: StudentSignUpInputValidationError,
            ) : PanelState
            data class TeacherSignUp(
                val signUpInput: TeacherSignUpInput,
                val validationError: TeacherSignUpInputValidationError,
            ) : PanelState
        }
        sealed interface MessageState {
            data object SignUpSuccess : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data class GoToFacultyPickerScreen(
            val mode: FacultySelectionMode,
            val facultyId: Int?,
            val batchId: Int?,
        ) : NavigationState
    }
}