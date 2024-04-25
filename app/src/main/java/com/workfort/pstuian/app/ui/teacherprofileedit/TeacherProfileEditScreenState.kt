package com.workfort.pstuian.app.ui.teacherprofileedit

import com.workfort.pstuian.model.FacultySelectionMode
import com.workfort.pstuian.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.model.TeacherConnectInfoInputError
import com.workfort.pstuian.model.TeacherProfile


data class TeacherProfileEditScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val panelState: PanelState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                panelState = PanelState.None,
                messageState = null,
            )
        }
        sealed interface PanelState {
            data object None : PanelState
            data object Loading : PanelState
            data class Academic(
                val profile: TeacherProfile,
                val validationError: TeacherAcademicInfoInputError,
            ) : PanelState
            data class Connect(
                val profile: TeacherProfile,
                val validationError: TeacherConnectInfoInputError,
            ) : PanelState
            data class Error(val message: String) : PanelState
        }
        sealed interface MessageState {
            data class Loading(val cancelable: Boolean) : MessageState
            data object ConfirmSave : MessageState
            data class Success(val message: String) : MessageState
            data class Error(val message: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object GoBack : NavigationState
        data class GoToFacultyPickerScreen(
            val mode: FacultySelectionMode,
            val facultyId: Int,
        ) : NavigationState
    }
}