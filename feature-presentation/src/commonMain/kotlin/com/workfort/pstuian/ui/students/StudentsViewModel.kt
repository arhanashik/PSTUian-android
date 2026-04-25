package com.workfort.pstuian.ui.students

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.students.state.StudentsMessageState
import com.workfort.pstuian.ui.students.state.StudentsNavigationState
import com.workfort.pstuian.ui.students.state.StudentsUiEvent
import com.workfort.pstuian.ui.students.state.StudentsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StudentsViewModel(
    private val batchId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: StudentsUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<StudentsUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<StudentsMessageState?>(null)
    val message: StateFlow<StudentsMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<StudentsNavigationState?>(null)
    val navigation: StateFlow<StudentsNavigationState?> = _navigation.asStateFlow()

    override fun onUiReady() {
        loadData()
    }

    fun onUiEvent(event: StudentsUiEvent) {
        when (event) {
            is StudentsUiEvent.BackClicked -> _navigation.update { StudentsNavigationState.GoBack }
            is StudentsUiEvent.StudentClicked -> onClickStudent(event.student)
            is StudentsUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickStudent(student: User.Student) {
        _navigation.update { StudentsNavigationState.GoToStudentProfile(student.studentId) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update {
            StudentsMessageState.ConfirmCall(phoneNumber) {
                // Do calling
            }
        }
    }

    private fun loadData() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showOperationLoading()
            facultyRepo.getBatch(batchId)
                .onSuccess { batch ->
                    uiStateMachine.showInitialState(title = batch.title ?: batch.name)
                    getStudents(batch.facultyId, batchId)
                }
                .onFailure {
                    uiStateMachine.showError(it.message ?: "Failed to load data")
                }
        }
    }

    private val studentListCache = mutableListOf<User.Student>()
    private var hasMoreData = true
    private fun getStudents(facultyId: Int, batchId: Int) {
        if (hasMoreData.not()) return

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            facultyRepo.getStudents(facultyId, batchId, forceRefresh = true)
                .onSuccess { students ->
                    studentListCache.clear()
                    studentListCache.addAll(students)
                    uiStateMachine.showStudents(studentListCache)
                }
                .onFailure {
                    uiStateMachine.showContentLoading(isLoading = false)
                    val message = it.message ?: "Failed to load students"
                    uiStateMachine.showError(message)
                }
        }
    }
}
