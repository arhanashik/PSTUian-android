package com.workfort.pstuian.ui.checkinlist.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface CheckInListNavigationState {
    data object GoBack : CheckInListNavigationState
    data class ProfileScreen(val userId: String, val userType: UserType) : CheckInListNavigationState
    data object LocationPickerScreen : CheckInListNavigationState
}
