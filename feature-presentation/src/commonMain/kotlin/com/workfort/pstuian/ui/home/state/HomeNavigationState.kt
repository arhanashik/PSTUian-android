package com.workfort.pstuian.ui.home.state

import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface HomeNavigationState {
    data object SplashScreen : HomeNavigationState
    data object SignInScreen : HomeNavigationState
    data class GoToProfileScreen(val userId: Int, val userType: UserType) : HomeNavigationState
    data object NotificationScreen : HomeNavigationState
    data class FacultyScreen(val faculty: Faculty) : HomeNavigationState
    data class ImagePreviewScreen(val url: String) : HomeNavigationState
    data object ContactUsScreen : HomeNavigationState
    data object DonorsScreen : HomeNavigationState
    data object BloodDonationRequestScreen : HomeNavigationState
    data object CheckInScreen : HomeNavigationState
    data object DonateScreen : HomeNavigationState
    data object SettingsScreen : HomeNavigationState
    data class Browser(val url: String) : HomeNavigationState
}
