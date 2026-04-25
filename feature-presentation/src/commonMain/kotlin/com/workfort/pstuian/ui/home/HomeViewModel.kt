package com.workfort.pstuian.ui.home

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.featuredomain.model.Slider
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.SliderRepository
import com.workfort.pstuian.featuredomain.usecase.ClearCacheUseCase
import com.workfort.pstuian.model.SharedScreenData
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.home.state.HomeMessageState
import com.workfort.pstuian.ui.home.state.HomeNavigationState
import com.workfort.pstuian.ui.home.state.HomeUiEvent
import com.workfort.pstuian.ui.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepository: AuthRepository,
    private val sliderRepo: SliderRepository,
    private val facultyRepo: FacultyRepository,
    private val settingsRepository: SettingsRepository,
    private val sharedScreenData: SharedScreenData,
    private val clearCacheUseCase: ClearCacheUseCase,
    private val uiStateMachine: HomeUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<HomeUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<HomeMessageState?>(null)
    val message: StateFlow<HomeMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<HomeNavigationState?>(null)
    val navigation: StateFlow<HomeNavigationState?> = _navigation.asStateFlow()

    private fun isSignedInUser(): Boolean = authRepository.isUserSignedIn()

    override fun onUiReady() {
        uiStateMachine.setInitialContent(isSignedInUser())
        observeSignedInUser()
        loadSliders()
        loadFaculties()
    }

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
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
                            HomeNavigationState.Browser(NetworkConst.Remote.PSTU_WEBSITE)
                        }
                    }
                    Action.Donors -> onClickDonors()
                    Action.VarsityWebsite -> {
                        _navigation.update {
                            HomeNavigationState.Browser(NetworkConst.Remote.PSTU_WEBSITE)
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

    private fun observeSignedInUser() {
        viewModelScope.launch (coroutineDispatcherProvider.io) {
            authRepository.observeSignedInAuthUser().collectLatest { authUser ->
                uiStateMachine.updateSignedInState(isSignedInUser = authUser != null)
            }
        }
    }

    private fun loadSliders() {
        uiStateMachine.showSliderLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            sliderRepo.getSliders()
                .onSuccess {
                    uiStateMachine.showSliders(it)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load slides"
                    uiStateMachine.showSliderError(message)
                }
        }
    }

    private fun loadFaculties() {
        uiStateMachine.showFacultyLoading()
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            facultyRepo.getFaculties()
                .onSuccess {
                    uiStateMachine.showFaculties(it)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load faculties"
                    uiStateMachine.showFacultyError(message)
                }
        }
    }

    fun onMessageHandled() {
        _message.update { null }
    }

    fun onNavigationHandled() {
        _navigation.update { null }
    }

    private fun onClickSignIn() {
        when (settingsRepository.getUserType()) {
            UserType.STUDENT, UserType.TEACHER -> {
                _navigation.update { HomeNavigationState.SignInScreen }
            }
            else -> {
                _message.update {
                    HomeMessageState.UserTypeSelectionForSignIn(
                        selectedUserType = settingsRepository.getUserType(),
                        onSaveAndContinue = { selected ->
                            when (selected) {
                                UserType.STUDENT, UserType.TEACHER -> {
                                    settingsRepository.setUserType(selected)
                                    _message.update { null }
                                    _navigation.update { HomeNavigationState.SignInScreen }
                                }
                                else -> {
                                    _message.update { null }
                                    _message.update {
                                        HomeMessageState.SignInNotSupportedForUserType(
                                            message = "Sign in is not yet supported for this user type.",
                                        )
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }

    private fun showNotificationPermissionConfirmation() {
        _message.update { HomeMessageState.NotificationPermission }
    }

    private fun onClickUserProfile() {
        val user = sharedScreenData.getCurrentUser()
        if (user == null) {
            _message.update { HomeMessageState.SignInNecessary }
        } else {
            val (userId, userType) = when (user) {
                is User.Student -> user.studentId to UserType.STUDENT
                is User.Teacher -> user.id to UserType.TEACHER
                is User.Employee -> (user.userId.toIntOrNull() ?: 0) to UserType.EMPLOYEE
            }

            _navigation.update { HomeNavigationState.GoToProfileScreen(userId, userType) }
        }
    }

    private fun onClickNotification() {
        _navigation.update { HomeNavigationState.NotificationScreen }
    }

    private fun onScrollSlider(position: Int) {
        uiStateMachine.updateSliderPosition(position)
    }

    private fun onClickSlider(slider: Slider) {
        slider.imageUrl?.let { imageUrl ->
            _navigation.update { HomeNavigationState.ImagePreviewScreen(imageUrl) }
        }
    }

    private fun onClickFaculty(faculty: Faculty) {
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

    fun clearAllData() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                clearCacheUseCase()
            }.onSuccess {
                _navigation.update { HomeNavigationState.SplashScreen }
            }.onFailure {
                val error = it.message ?: "Failed to clear data"
                _message.update { HomeMessageState.ClearAllDataFailed(error) }
            }
        }
    }
}
