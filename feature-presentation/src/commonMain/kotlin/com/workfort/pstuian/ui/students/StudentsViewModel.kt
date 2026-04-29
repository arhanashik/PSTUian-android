package com.workfort.pstuian.ui.students

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Batch
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

    private var batchCache: Batch? = null
    private val studentListCache = mutableListOf<User.Student>()
    private var currentPage = 1
    private var hasMoreData = true

    override fun onUiReady() {
        loadData()
    }

    fun onUiEvent(event: StudentsUiEvent) {
        when (event) {
            is StudentsUiEvent.BackClicked -> _navigation.update { StudentsNavigationState.GoBack }
            is StudentsUiEvent.Refresh -> batchCache?.let {
                getStudents(it.facultyId, batchId, refresh = true)
            }
            is StudentsUiEvent.LoadMore -> batchCache?.let {
                getStudents(it.facultyId, batchId, refresh = false)
            }
            is StudentsUiEvent.StudentClicked -> onClickStudent(event.student)
            is StudentsUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickStudent(student: User.Student) {
        _navigation.update { StudentsNavigationState.GoToStudentProfile(student.id) }
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
                    batchCache = batch
                    uiStateMachine.showInitialState(title = batch.title ?: batch.name)
                    getStudents(batch.facultyId, batchId, refresh = false)
                }
                .onFailure {
                    uiStateMachine.showError(it.message ?: "Failed to load data")
                }
        }
    }

    private fun getStudents(facultyId: Int, batchId: Int, refresh: Boolean) {
        if (refresh) {
            studentListCache.clear()
            currentPage = 1
            hasMoreData = true
        } else if (!hasMoreData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showContentLoading(isLoading = true)
            facultyRepo.getStudents(facultyId, batchId, currentPage, useCache = !refresh)
                .onSuccess { students ->
                    if (students.isEmpty()) {
                        hasMoreData = false
                    } else {
                        currentPage++
                    }
                    studentListCache.addAll(students)
                    uiStateMachine.showStudents(studentListCache)
                }
                .onFailure {
                    uiStateMachine.showContentLoading(isLoading = false)
                    if (studentListCache.isEmpty()) {
                        val message = it.message ?: "Failed to load students"
                        uiStateMachine.showError(message)
                    }
                }
        }
    }
}
