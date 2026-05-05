package com.workfort.pstuian.ui.checkin.state

import com.workfort.pstuian.featuredomain.model.UserType

sealed interface CheckInNavigationState {
    data object GoBack : CheckInNavigationState
    data class ProfileScreen(val userId: Int, val userType: UserType) : CheckInNavigationState
    data object LocationPickerScreen : CheckInNavigationState
}
