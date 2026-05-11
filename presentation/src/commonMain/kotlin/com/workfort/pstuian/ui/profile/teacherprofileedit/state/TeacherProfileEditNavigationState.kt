package com.workfort.pstuian.ui.profile.teacherprofileedit.state

sealed interface TeacherProfileEditNavigationState {
    data object GoBack : TeacherProfileEditNavigationState
}
