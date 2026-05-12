package com.workfort.pstuian.ui.faculty.teacher

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherMessageState
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherNavigationState
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherUiEvent
import com.workfort.pstuian.ui.faculty.teacher.state.TeacherUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TeacherViewModel(
    private val facultyId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: TeacherUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<TeacherUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<TeacherMessageState?>(null)
    val message: StateFlow<TeacherMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<TeacherNavigationState?>(null)
    val navigation: StateFlow<TeacherNavigationState?> = _navigation.asStateFlow()

    private val teacherListCache = mutableListOf<User.Teacher>()
    private var currentPage = 1
    private var hasMoreData = true

    override fun onUiReady() {
        getTeachers(forceRefresh = false)
    }

    fun onUiEvent(event: TeacherUiEvent) {
        when (event) {
            is TeacherUiEvent.LoadMore -> getTeachers(forceRefresh = false)
            is TeacherUiEvent.TeacherClicked -> onClickTeacher(event.teacher)
            is TeacherUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickTeacher(teacher: User.Teacher) {
        _navigation.update { TeacherNavigationState.GoToTeacherProfile(teacher.userId) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update {
            TeacherMessageState.ConfirmCall(phoneNumber) {
                // Do calling
            }
        }
    }

    private fun getTeachers(forceRefresh: Boolean) {
        if (forceRefresh) {
            teacherListCache.clear()
            currentPage = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            facultyRepo.getTeachers(facultyId, currentPage, forceRefresh)
                .onSuccess { teachers ->
                    if (teachers.isEmpty()) {
                        hasMoreData = false
                    } else {
                        currentPage++
                    }
                    teacherListCache.addAll(teachers)
                    uiStateMachine.showTeachers(teacherListCache.toList())
                }
                .onFailure {
                    uiStateMachine.showContentLoading(isLoading = false)
                    if (teacherListCache.isEmpty()) {
                        val message = it.message ?: "Failed to load teachers"
                        uiStateMachine.showError(message)
                    }
                }
        }
    }
}
