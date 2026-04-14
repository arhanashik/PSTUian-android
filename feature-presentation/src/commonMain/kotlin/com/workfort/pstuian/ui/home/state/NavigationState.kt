package com.workfort.pstuian.ui.home.state

import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.UserType

sealed interface NavigationState {
    data object SplashScreen : NavigationState
    data object SignInScreen : NavigationState
    data class GoToProfileScreen(val userType: UserType, val userId: Int) : NavigationState
    data object NotificationScreen : NavigationState
    data class FacultyScreen(val faculty: FacultyEntity) : NavigationState
    data class ImagePreviewScreen(val url: String) : NavigationState
    data object ContactUsScreen : NavigationState
    data object DonorsScreen : NavigationState
    data object BloodDonationRequestScreen : NavigationState
    data object CheckInScreen : NavigationState
    data object DonateScreen : NavigationState
    data object SettingsScreen : NavigationState
}
