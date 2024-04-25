package com.workfort.pstuian.app.ui.employeeprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.EmployeeProfile
import com.workfort.pstuian.repository.FacultyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class EmployeeProfileViewModel(
    private val userId: Int,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: EmployeeProfileScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<EmployeeProfileScreenState> get() = _screenState

    private fun updateScreenState(update: EmployeeProfileScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(EmployeeProfileScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(
        EmployeeProfileScreenStateUpdate.NavigationConsumed
    )

    fun onClickBack() = updateScreenState(
        EmployeeProfileScreenStateUpdate.NavigateTo(
            EmployeeProfileScreenState.NavigationState.GoBack
        )
    )

    fun onClickCall() = profileCache()?.emplyee?.phone?.let { phoneNumber ->
        if (phoneNumber.isEmpty()) return@let
        updateScreenState(
            EmployeeProfileScreenStateUpdate.UpdateMessageState(
                EmployeeProfileScreenState.DisplayState.MessageState.Call(phoneNumber)
            )
        )
    }

    fun onClickTab(index: Int) = updateScreenState(
        EmployeeProfileScreenStateUpdate.UpdateSelectedTab(index)
    )

    fun onClickImage(url: String) {
        val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        updateScreenState(
            EmployeeProfileScreenStateUpdate.NavigateTo(
                EmployeeProfileScreenState.NavigationState.ImagePreviewScreen(encodedUrl),
            )
        )
    }

    fun onClickBio() = Unit

    private fun profileCache(): EmployeeProfile? {
        return when (val state = _screenState.value.displayState.profileState) {
            is EmployeeProfileScreenState.DisplayState.ProfileState.Available -> state.profile
            else -> null
        }
    }

    fun loadProfile() {
        viewModelScope.launch {
            updateScreenState(EmployeeProfileScreenStateUpdate.ProfileLoading)
            runCatching {
                val profile = withContext(Dispatchers.IO) {
                    facultyRepo.getEmployeeProfile(userId)
                }
                updateScreenState(EmployeeProfileScreenStateUpdate.ProfileLoaded(profile))
            }.onFailure {
                it.printStackTrace()
                val message = it.message ?: "Failed to load profile"
                updateScreenState(EmployeeProfileScreenStateUpdate.ProfileLoadFailed(message))
            }
        }
    }
}