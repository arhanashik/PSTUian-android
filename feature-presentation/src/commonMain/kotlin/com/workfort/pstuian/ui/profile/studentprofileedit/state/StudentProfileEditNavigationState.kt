package com.workfort.pstuian.ui.profile.studentprofileedit.state

sealed interface StudentProfileEditNavigationState {
    data object GoBack : StudentProfileEditNavigationState
}
