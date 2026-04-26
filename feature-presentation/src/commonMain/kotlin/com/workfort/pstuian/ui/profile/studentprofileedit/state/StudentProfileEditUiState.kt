package com.workfort.pstuian.ui.profile.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface StudentProfileEditUiState {
    data object None : StudentProfileEditUiState

    data class Content(
        val panelState: PanelState = PanelState.None,
    ) : StudentProfileEditUiState

    sealed interface PanelState {
        data object None : PanelState
        data object Loading : PanelState
        data class Academic(
            val profile: UserProfile.StudentProfile,
            val validationError: StudentAcademicInfoInputError,
        ) : PanelState
        data class Connect(
            val profile: UserProfile.StudentProfile,
            val validationError: StudentConnectInfoInputError,
        ) : PanelState
        data class Error(val message: String) : PanelState
    }
}
