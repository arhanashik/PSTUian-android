package com.workfort.pstuian.ui.employeeprofile

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.InitializationMode
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.model.EmployeeProfile
import com.workfort.pstuian.ui.employeeprofile.state.EmployeeProfileUiState
import com.workfort.pstuian.ui.employeeprofile.state.MessageState
import com.workfort.pstuian.ui.employeeprofile.state.NavigationState
import com.workfort.pstuian.ui.employeeprofile.state.ProfileState
import kotlinx.coroutines.launch

internal class EmployeeProfileViewModel(
    private val userId: Int,
    private val facultyRepo: FacultyRepositoryImpl,
    private val uiStateMachine: EmployeeProfileUiStateMachine,
) : UiStateMachineViewModel<EmployeeProfileUiState>(
    uiStateMachine,
    initializationMode = InitializationMode.JustOnce,
) {

    override fun onUiReady() {
        loadProfile()
    }

    fun messageConsumed() = uiStateMachine.showMessage(null)

    fun navigationConsumed() = uiStateMachine.navigateTo(null)

    fun onClickBack() = uiStateMachine.navigateTo(NavigationState.GoBack)

    fun onClickCall() = profileCache()?.employee?.phone?.let { phoneNumber ->
        if (phoneNumber.isNotEmpty()) {
            uiStateMachine.showMessage(MessageState.Call(phoneNumber))
        }
    }

    fun onClickTab(index: Int) {
        uiStateMachine.updateSelectedTab(index)
    }

    fun onClickImage(url: String) {
        uiStateMachine.navigateTo(NavigationState.ImagePreviewScreen(url))
    }

    private fun profileCache(): EmployeeProfile? {
        return when (val state = uiState.value.profileState) {
            is ProfileState.Available -> state.profile
            else -> null
        }
    }

    fun loadProfile() {
        getProfile(userId)
    }

    private fun getProfile(employeeId: Int) {
        uiStateMachine.showProfileLoading()
        viewModelScope.launch {
            runCatching {
                facultyRepo.getEmployeeProfile(employeeId)
            }.onSuccess {
                uiStateMachine.showProfile(it)
            }.onFailure {
                val message = it.message ?: "Failed to load employee profile"
                uiStateMachine.showProfileError(message)
            }
        }
    }
}
