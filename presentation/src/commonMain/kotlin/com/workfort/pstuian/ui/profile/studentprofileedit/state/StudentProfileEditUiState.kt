package com.workfort.pstuian.ui.profile.studentprofileedit.state

import com.workfort.pstuian.featuredomain.model.StudentAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.StudentConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface StudentProfileEditUiState {
    data object None : StudentProfileEditUiState

    data class Content(
        val profile: UserProfile.StudentProfile,
        val academicInfoInputError: StudentAcademicInfoInputError = StudentAcademicInfoInputError.INITIAL,
        val connectInfoInputError: StudentConnectInfoInputError = StudentConnectInfoInputError.INITIAL,
        val selectedTabIndex: Int = 0,
        val isLoading: Boolean = false,
    ) : StudentProfileEditUiState
}
