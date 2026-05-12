package com.workfort.pstuian.ui.faculty.faculty.state

sealed interface FacultyNavigationState {
    data object GoBack : FacultyNavigationState
}
