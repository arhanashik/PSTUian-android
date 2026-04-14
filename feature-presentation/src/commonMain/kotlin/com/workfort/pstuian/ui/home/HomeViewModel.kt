package com.workfort.pstuian.ui.home

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.FacultyEntity
import com.workfort.pstuian.featuredomain.model.SliderEntity
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.SliderRepository
import com.workfort.pstuian.featuredomain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.ui.home.state.HomeUiState
import com.workfort.pstuian.ui.home.state.HomeMessageState
import com.workfort.pstuian.ui.home.state.HomeNavigationState
import com.workfort.pstuian.ui.home.state.HomeUiEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val authRepo: AuthRepository,
    private val sliderRepo: SliderRepository,
    private val facultyRepo: FacultyRepository,
    private val clearAllDataUseCase: ClearAllDataUseCase,
    private val uiStateMachine: HomeUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<HomeUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<HomeMessageState?>(null)
    val message: StateFlow<HomeMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<HomeNavigationState?>(null)
    val navigation: StateFlow<HomeNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
        loadInitialData()
    }

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LoadInitialData -> loadInitialData()
            is HomeUiEvent.GetSliders -> getSliders()
            is HomeUiEvent.GetFaculties -> getFaculties()
            is HomeUiEvent.GetUserProfile -> getUserProfile()
            is HomeUiEvent.SignInClicked -> onClickSignIn()
            is HomeUiEvent.UserProfileClicked -> onClickUserProfile()
            is HomeUiEvent.NotificationClicked -> onClickNotification()
            is HomeUiEvent.ScrollSlider -> onScrollSlider(event.position)
            is HomeUiEvent.SliderClicked -> onClickSlider(event.slider)
            is HomeUiEvent.FacultyClicked -> onClickFaculty(event.faculty)
            is HomeUiEvent.ActionItemClicked -> {
                when (event.actionItem.action) {
                    Action.AdmissionSupport -> {
                        _navigation.update {
                            HomeNavigationState.Browser(com.workfort.pstuian.appconstant.NetworkConst.Remote.PSTU_WEBSITE)
                        }
                    }
                    Action.Donors -> onClickDonors()
                    Action.VarsityWebsite -> {
                        _navigation.update {
                            HomeNavigationState.Browser(com.workfort.pstuian.appconstant.NetworkConst.Remote.PSTU_WEBSITE)
                        }
                    }
                    Action.ContactUs -> onClickContactUs()
                    Action.RequestBloodDonation -> onClickRequestBloodDonation()
                    Action.CheckIn -> onClickCheckIn()
                    Action.RateApp -> {
                        _navigation.update { HomeNavigationState.Store }
                    }
                    Action.ClearData -> onClickClearData()
                    Action.Settings -> onClickSettings()
                    Action.Donate -> onClickDonate()
                }
            }
            is HomeUiEvent.RequestNotificationPermissionClicked -> showNotificationPermissionConfirmation()
            is HomeUiEvent.ClearDataClicked -> clearAllData()
        }
    }

    private fun isSignedInUser(): Boolean {
        val state = uiState.value
        return state is HomeUiState.Content && state.profileState is HomeUiState.ProfileState.Available
    }

    fun onMessageHandled() {
        _message.update { null }
    }

    fun onNavigationHandled() {
        _navigation.update { null }
    }

    private fun onClickSignIn() {
        onMessageHandled()
        _navigation.update { HomeNavigationState.SignInScreen }
    }

    private fun showNotificationPermissionConfirmation() {
        _message.update { HomeMessageState.NotificationPermission }
    }

    private fun onClickUserProfile() {
        val state = uiState.value
        if (state is HomeUiState.Content && state.profileState is HomeUiState.ProfileState.Available) {
            val user = state.profileState.user
            val userType = when (user) {
                is StudentEntity -> UserType.STUDENT
                is TeacherEntity -> UserType.TEACHER
                else -> null
            }
            val userId = when (user) {
                is StudentEntity -> user.id
                is TeacherEntity -> user.id
                else -> null
            }
            if (userType != null && userId != null) {
                _navigation.update { HomeNavigationState.GoToProfileScreen(userType, userId) }
            }
        }
    }

    private fun onClickNotification() {
        _navigation.update { HomeNavigationState.NotificationScreen }
    }

    private fun onScrollSlider(position: Int) {
        uiStateMachine.updateSliderPosition(position)
    }

    private fun onClickSlider(slider: SliderEntity) {
        slider.imageUrl?.let { imageUrl ->
            _navigation.update { HomeNavigationState.ImagePreviewScreen(imageUrl) }
        }
    }

    private fun onClickFaculty(faculty: FacultyEntity) {
        if (isSignedInUser()) {
            _navigation.update { HomeNavigationState.FacultyScreen(faculty) }
        } else {
            _message.update { HomeMessageState.SignInNecessary }
        }
    }

    private fun onClickDonors() {
        _navigation.update { HomeNavigationState.DonorsScreen }
    }

    private fun onClickContactUs() {
        _navigation.update { HomeNavigationState.ContactUsScreen }
    }

    private fun onClickRequestBloodDonation() {
        if (isSignedInUser()) {
            _navigation.update { HomeNavigationState.BloodDonationRequestScreen }
        } else {
            _message.update { HomeMessageState.SignInNecessary }
        }
    }

    private fun onClickCheckIn() {
        if (isSignedInUser()) {
            _navigation.update { HomeNavigationState.CheckInScreen }
        } else {
            _message.update { HomeMessageState.SignInNecessary }
        }
    }

    private fun onClickSettings() {
        _navigation.update { HomeNavigationState.SettingsScreen }
    }

    private fun onClickDonate() {
        _navigation.update { HomeNavigationState.DonateScreen }
    }

    private fun onClickClearData() {
        _message.update { HomeMessageState.ClearAllData }
    }

    private fun loadInitialData() {
        getSliders()
        getFaculties()
        getUserProfile()
    }

    fun getSliders() {
        uiStateMachine.showSliderLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                sliderRepo.getSliders()
            }.onSuccess {
                uiStateMachine.showSliders(it)
            }.onFailure {
                val message = it.message ?: "Failed to load slides"
                uiStateMachine.showSliderError(message)
            }
        }
    }

    fun getFaculties() {
        uiStateMachine.showFacultyLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                facultyRepo.getFaculties()
            }.onSuccess {
                uiStateMachine.showFaculties(it)
            }.onFailure {
                val message = it.message ?: "Failed to load faculties"
                uiStateMachine.showFacultyError(message)
            }
        }
    }

    fun getUserProfile() {
        uiStateMachine.showProfileLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                authRepo.getSignInUser()
            }.onSuccess {
                uiStateMachine.showProfile(it)
            }.onFailure {
                val message = it.message ?: "Failed to load profile"
                uiStateMachine.showProfileError(message)
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                clearAllDataUseCase()
            }.onSuccess {
                _navigation.update { HomeNavigationState.SplashScreen }
            }.onFailure {
                val error = it.message ?: "Failed to clear data"
                _message.update { HomeMessageState.ClearAllDataFailed(error) }
            }
        }
    }
}
