package com.workfort.pstuian.ui.home

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
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
import com.workfort.pstuian.ui.home.state.MessageState
import com.workfort.pstuian.ui.home.state.NavigationState
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
) : UiStateMachineViewModel<HomeUiState>(uiStateMachine) {

    private val _messageState = MutableStateFlow<MessageState?>(null)
    val messageState: StateFlow<MessageState?> = _messageState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState?>(null)
    val navigationState: StateFlow<NavigationState?> = _navigationState.asStateFlow()

    override fun onUiReady() {
        uiStateMachine.setInitialContent()
        loadInitialData()
    }

    private fun isSignedInUser(): Boolean {
        val state = uiState.value
        return state is HomeUiState.Content && state.profileState is HomeUiState.ProfileState.Available
    }

    fun messageConsumed() {
        _messageState.update { null }
    }

    fun navigationConsumed() {
        _navigationState.update { null }
    }

    fun onClickSignIn() {
        messageConsumed()
        _navigationState.update { NavigationState.SignInScreen }
    }

    fun showNotificationPermissionConfirmation() {
        _messageState.update { MessageState.NotificationPermission }
    }

    fun onClickUserProfile() {
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
                _navigationState.update { NavigationState.GoToProfileScreen(userType, userId) }
            }
        }
    }

    fun onClickNotification() {
        _navigationState.update { NavigationState.NotificationScreen }
    }

    fun onScrollSlider(position: Int) {
        uiStateMachine.updateSliderPosition(position)
    }

    fun onClickSlider(slider: SliderEntity) {
        slider.imageUrl?.let { imageUrl ->
            _navigationState.update { NavigationState.ImagePreviewScreen(imageUrl) }
        }
    }

    fun onClickFaculty(faculty: FacultyEntity) {
        if (isSignedInUser()) {
            _navigationState.update { NavigationState.FacultyScreen(faculty) }
        } else {
            _messageState.update { MessageState.SignInNecessary }
        }
    }

    fun onClickDonors() {
        _navigationState.update { NavigationState.DonorsScreen }
    }

    fun onClickContactUs() {
        _navigationState.update { NavigationState.ContactUsScreen }
    }

    fun onClickRequestBloodDonation() {
        if (isSignedInUser()) {
            _navigationState.update { NavigationState.BloodDonationRequestScreen }
        } else {
            _messageState.update { MessageState.SignInNecessary }
        }
    }

    fun onClickCheckIn() {
        if (isSignedInUser()) {
            _navigationState.update { NavigationState.CheckInScreen }
        } else {
            _messageState.update { MessageState.SignInNecessary }
        }
    }

    fun onClickSettings() {
        _navigationState.update { NavigationState.SettingsScreen }
    }

    fun onClickDonate() {
        _navigationState.update { NavigationState.DonateScreen }
    }

    fun onClickClearData() {
        _messageState.update { MessageState.ClearAllData }
    }

    private fun loadInitialData() {
        getSliders()
        getFaculties()
        getUserProfile()
    }

    fun getSliders() {
        uiStateMachine.showSliderLoading()
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
            runCatching {
                clearAllDataUseCase()
            }.onSuccess {
                _navigationState.update { NavigationState.SplashScreen }
            }.onFailure {
                val error = it.message ?: "Failed to clear data"
                _messageState.update { MessageState.ClearAllDataFailed(error) }
            }
        }
    }
}
