package com.workfort.pstuian.ui.faculty.state

sealed interface FacultyNavigationState {
    data object GoBack : FacultyNavigationState
}
