package com.workfort.pstuian.app.ui.commonmodel.home

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.model.SliderEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.model.UserType
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.SliderRepository
import com.workfort.pstuian.reducer.ui.home.HomeScreenState
import com.workfort.pstuian.reducer.ui.home.HomeScreenStateReducer
import com.workfort.pstuian.reducer.ui.home.HomeScreenStateUpdate
import com.workfort.pstuian.usecase.ClearAllDataUseCase
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepo: AuthRepository,
    private val sliderRepo: SliderRepository,
    private val facultyRepo: FacultyRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val stateReducer: HomeScreenStateReducer,
) : BaseViewModel() {

    private val _homeScreenState = MutableStateFlow(stateReducer.initial)
    val homeScreenState: StateFlow<HomeScreenState> get() = _homeScreenState

    private fun updateScreenState(update: HomeScreenStateUpdate) =
        _homeScreenState.update { oldState -> stateReducer.reduce(oldState, update) }

    private fun isSignedInUser() = _homeScreenState.value.displayState.profileState is
            HomeScreenState.DisplayState.ProfileState.Available

    fun messageConsumed() = updateScreenState(HomeScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(HomeScreenStateUpdate.NavigationConsumed)

    fun onClickSignIn() {
        messageConsumed()
        updateScreenState(
            HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.SignInScreen),
        )
    }

    fun showNotificationPermissionConfirmation() {
        updateScreenState(
            HomeScreenStateUpdate.UpdateMessageState(
                HomeScreenState.DisplayState.MessageState.NotificationPermission,
            ),
        )
    }

    fun onClickUserProfile() {
        val state = _homeScreenState.value.displayState.profileState
        if (state is HomeScreenState.DisplayState.ProfileState.Available) {
            val userType = when (state.user) {
                is StudentEntity -> UserType.STUDENT
                is TeacherEntity -> UserType.TEACHER
                else -> null
            }
            val userId = when (state.user) {
                is StudentEntity -> state.user.id
                is TeacherEntity -> state.user.id
                else -> null
            }
            if (userType != null && userId != null) {
                updateScreenState(
                    HomeScreenStateUpdate.NavigateTo(
                        HomeScreenState.NavigationState.GoToProfileScreen(userType, userId),
                    ),
                )
            }
        }
    }

    fun onClickNotification() = updateScreenState(
        HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.NotificationScreen)
    )

    fun onScrollSlider(position: Int) {
        updateScreenState(HomeScreenStateUpdate.UpdateSliderPosition(position))
    }

    fun onClickSlider(slider: SliderEntity) = slider.imageUrl?.let { imageUrl ->
        updateScreenState(
            HomeScreenStateUpdate.NavigateTo(
                HomeScreenState.NavigationState.ImagePreviewScreen(imageUrl)
            )
        )
    }

    fun onClickFaculty(faculty: FacultyEntity) = updateScreenState(
        if (isSignedInUser()) {
            HomeScreenStateUpdate.NavigateTo(
                HomeScreenState.NavigationState.FacultyScreen(faculty)
            )
        } else {
            HomeScreenStateUpdate.UpdateMessageState(
                HomeScreenState.DisplayState.MessageState.SignInNecessary
            )
        }
    )

    fun onClickDonors() = updateScreenState(
        HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.DonorsScreen)
    )

    fun onClickContactUs() = updateScreenState(
        HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.ContactUsScreen)
    )

    fun onClickRequestBloodDonation() {
        val state = if (isSignedInUser()) {
            HomeScreenStateUpdate.NavigateTo(
                HomeScreenState.NavigationState.BloodDonationRequestScreen,
            )
        } else {
            HomeScreenStateUpdate.UpdateMessageState(
                HomeScreenState.DisplayState.MessageState.SignInNecessary
            )
        }
        updateScreenState(state)
    }

    fun onClickCheckIn() {
        val state = if (isSignedInUser()) {
            HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.CheckInScreen)
        } else {
            HomeScreenStateUpdate.UpdateMessageState(
                HomeScreenState.DisplayState.MessageState.SignInNecessary
            )
        }
        updateScreenState(state)
    }

    fun onClickSettings() = updateScreenState(
        HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.SettingsScreen)
    )

    fun onClickDonate() = updateScreenState(
        HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.DonateScreen)
    )

    fun onClickClearData() = updateScreenState(
        HomeScreenStateUpdate.UpdateMessageState(
            HomeScreenState.DisplayState.MessageState.ClearAllData
        )
    )

    fun loadInitialData() {
        getSliders()
        getFaculties()
        getUserProfile()
    }

    fun getSliders() {
        updateScreenState(HomeScreenStateUpdate.SliderLoading)
        viewModelScope.launch {
            runCatching {
                sliderRepo.getSliders()
            }.onSuccess {
                updateScreenState(HomeScreenStateUpdate.SliderLoaded(it))
            }.onFailure {
                val message = it.message ?: "Failed to load slides"
                updateScreenState(HomeScreenStateUpdate.SliderLoadFailed(message))
            }
        }
    }

    fun getFaculties() {
        viewModelScope.launch {
            updateScreenState(HomeScreenStateUpdate.FacultyLoading)
            runCatching {
                val faculties = facultyRepo.getFaculties()
                updateScreenState(HomeScreenStateUpdate.FacultyLoaded(faculties))
            }.onFailure {
                val message = it.message ?: "Failed to load faculties"
                updateScreenState(HomeScreenStateUpdate.FacultyLoadFailed(message))
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            updateScreenState(HomeScreenStateUpdate.ProfileLoading)
            runCatching {
                val user = authRepo.getSignInUser()
                updateScreenState(HomeScreenStateUpdate.ProfileLoaded(user))
            }.onFailure {
                val message = it.message ?: "Failed to load profile"
                updateScreenState(HomeScreenStateUpdate.ProfileLoadFailed(message))
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            runCatching {
                clearAllDataUseCase()
            }.onSuccess {
                updateScreenState(
                    HomeScreenStateUpdate.NavigateTo(HomeScreenState.NavigationState.SplashScreen),
                )
            }.onFailure {
                val error = it.message ?: "Failed to clear data"
                updateScreenState(
                    HomeScreenStateUpdate.UpdateMessageState(
                        HomeScreenState.DisplayState.MessageState.ClearAllDataFailed(error)
                    )
                )
            }
        }
    }
}
