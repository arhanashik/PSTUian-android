package com.workfort.pstuian.ui.profile.teacherprofileedit.state

import com.workfort.pstuian.featuredomain.model.TeacherAcademicInfoInputError
import com.workfort.pstuian.featuredomain.model.TeacherConnectInfoInputError
import com.workfort.pstuian.featuredomain.model.UserProfile

sealed interface TeacherProfileEditUiState {
    data object None : TeacherProfileEditUiState

    data class Content(
        val profile: UserProfile.TeacherProfile,
        val academicInfoInputError: TeacherAcademicInfoInputError = TeacherAcademicInfoInputError.INITIAL,
        val connectInfoInputError: TeacherConnectInfoInputError = TeacherConnectInfoInputError.INITIAL,
        val selectedTabIndex: Int = 0,
        val isLoading: Boolean = false,
    ) : TeacherProfileEditUiState
}
