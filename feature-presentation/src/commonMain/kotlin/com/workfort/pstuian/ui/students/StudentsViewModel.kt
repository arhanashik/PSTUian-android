package com.workfort.pstuian.ui.students

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.common.uistate.InitializationMode
import com.workfort.pstuian.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.ui.students.state.MessageState
import com.workfort.pstuian.ui.students.state.NavigationState
import com.workfort.pstuian.ui.students.state.StudentsUiState
import kotlinx.coroutines.launch

internal class StudentsViewModel(
    private val batchId: Int,
    private val facultyRepo: FacultyRepository,
    private val uiStateMachine: StudentsUiStateMachine,
) : UiStateMachineViewModel<StudentsUiState>(
    uiStateMachine,
    initializationMode = InitializationMode.JustOnce,
) {

    override fun onUiReady() {
        loadStudentList()
    }

    fun messageConsumed() {
        uiStateMachine.showMessage(null)
    }

    fun navigationConsumed() {
        uiStateMachine.navigateTo(null)
    }

    fun onClickBack() {
        uiStateMachine.navigateTo(NavigationState.GoBack)
    }

    fun onClickStudent(student: StudentEntity) {
        uiStateMachine.navigateTo(NavigationState.GoToStudentProfile(student))
    }

    fun onClickCall(phoneNumber: String) {
        uiStateMachine.showMessage(MessageState.Call(phoneNumber))
    }

    fun loadStudentList() {
        viewModelScope.launch {
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
        viewModelScope.launch {
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
