package com.workfort.pstuian.ui.teacherprofileedit.state

import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherProfile

sealed interface TeacherProfileEditUiState {
    data object None : TeacherProfileEditUiState

    data class Content(
        val panelState: PanelState = PanelState.None,
    ) : TeacherProfileEditUiState

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
}
