package com.workfort.pstuian.ui.students

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.students.state.StudentsMessageState
import com.workfort.pstuian.ui.students.state.StudentsNavigationState
import com.workfort.pstuian.ui.students.state.StudentsUiEvent
import com.workfort.pstuian.ui.students.state.StudentsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class StudentsViewModel(
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
        loadStudentList()
    }

    fun onUiEvent(event: StudentsUiEvent) {
        when (event) {
            is StudentsUiEvent.LoadStudentList -> loadStudentList()
            is StudentsUiEvent.BackClicked -> onClickBack()
            is StudentsUiEvent.StudentClicked -> onClickStudent(event.student)
            is StudentsUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationHandled() = _navigation.update { null }

    private fun onClickBack() {
        _navigation.update { StudentsNavigationState.GoBack }
    }

    private fun onClickStudent(student: StudentEntity) {
        _navigation.update { StudentsNavigationState.GoToStudentProfile(student.id) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update {
            StudentsMessageState.ConfirmCall(phoneNumber) {
                // Do calling
            }
        }
    }

    private fun loadStudentList() {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            runCatching {
                val batch = facultyRepo.getBatch(batchId)
                uiStateMachine.updateTitle(batch.title ?: batch.name)
                getStudents(batch.facultyId, batchId)
            }.onFailure {
                uiStateMachine.updateTitle("Batch")
            }
        }
    }

    private val studentListCache = arrayListOf<StudentEntity>()
    private var hasMoreData = true
    private fun getStudents(facultyId: Int, batchId: Int) {
        if (hasMoreData.not()) return
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.showLoading(true)
            if (studentListCache.isNotEmpty()) {
                uiStateMachine.showContent(studentListCache.toList())
            }

            runCatching {
                val students = facultyRepo.getStudents(facultyId, batchId, forceRefresh = true)
                studentListCache.clear()
                studentListCache.addAll(students)
                uiStateMachine.showContent(studentListCache.toList())
            }.onFailure {
                val message = it.message ?: "Failed to load students"
                hasMoreData = false
                if (studentListCache.isEmpty()) {
                    uiStateMachine.showError(message)
                } else {
                    uiStateMachine.showLoading(false)
                }
            }
        }
    }
}
