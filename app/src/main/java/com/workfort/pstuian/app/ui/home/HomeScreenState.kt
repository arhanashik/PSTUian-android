package com.workfort.pstuian.app.ui.home

import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.model.UserType


data class HomeScreenState(
    val displayState: DisplayState = DisplayState.INITIAL,
    val navigationState: NavigationState? = null,
) {
    data class DisplayState(
        val profileState: ProfileState,
        val sliderState: SliderState,
        val facultyState: FacultyState,
        val messageState: MessageState?,
    ) {
        companion object {
            val INITIAL = DisplayState(
                profileState = ProfileState.None,
                sliderState = SliderState.None,
                facultyState = FacultyState.None,
                messageState = null,
            )
        }

        sealed interface ProfileState {
            data object None : ProfileState
            data object Loading : ProfileState
            data class Available(val user: Any) : ProfileState
            data class Error(val message: String) : ProfileState
        }

        sealed interface SliderState {
            data object None : SliderState
            data object Loading : SliderState
            data class Available(
                val sliders: List<SliderEntity>,
                val scrollPosition: Int,
            ) : SliderState
            data class Error(val message: String) : SliderState
        }

        sealed interface FacultyState {
            data object None : FacultyState
            data object Loading : FacultyState
            data class Available(val faculties: List<FacultyEntity>) : FacultyState
            data class Error(val message: String) : FacultyState
        }

        sealed interface MessageState {
            data object SignInNecessary : MessageState
            data object NotificationPermission : MessageState
            data object ClearAllData : MessageState
            data class ClearAllDataFailed(val error: String) : MessageState
        }
    }

    sealed interface NavigationState {
        data object SplashScreen : NavigationState
        data object SignInScreen : NavigationState
        data class GoToProfileScreen(
            val userType: UserType,
            val userId: Int,
        ) : NavigationState
        data object NotificationScreen : NavigationState
        data class FacultyScreen(val faculty: FacultyEntity) : NavigationState
        data class ImagePreviewScreen(val encodedImageUrl: String) : NavigationState
        data object ContactUsScreen : NavigationState
        data object DonorsScreen : NavigationState
        data object BloodDonationRequestScreen : NavigationState
        data object CheckInScreen : NavigationState
        data object DonateScreen : NavigationState
        data object SettingsScreen : NavigationState
    }
}