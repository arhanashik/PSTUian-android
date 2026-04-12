package com.workfort.pstuian.app.ui.commonmodel.employeeprofile

import com.workfort.pstuian.model.EmployeeProfile
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.reducer.ui.employeeprofile.EmployeeProfileScreenState
import com.workfort.pstuian.reducer.ui.employeeprofile.EmployeeProfileScreenStateReducer
import com.workfort.pstuian.reducer.ui.employeeprofile.EmployeeProfileScreenStateUpdate
import com.workfort.pstuian.app.ui.commonmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmployeeProfileViewModel(
    private val userId: Int,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: EmployeeProfileScreenStateReducer,
) : BaseViewModel() {

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

    fun onClickCall() = profileCache()?.employee?.phone?.let { phoneNumber ->
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
        // Encoding deferred to UI layer or use a KMP compatible way if needed
        updateScreenState(
            EmployeeProfileScreenStateUpdate.NavigateTo(
                EmployeeProfileScreenState.NavigationState.ImagePreviewScreen(url),
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
                facultyRepo.getEmployeeProfile(userId)
            }.onSuccess {
                updateScreenState(EmployeeProfileScreenStateUpdate.ProfileLoaded(it))
            }.onFailure {
                val message = it.message ?: "Failed to load profile"
                updateScreenState(EmployeeProfileScreenStateUpdate.ProfileLoadFailed(message))
            }
        }
    }
}
