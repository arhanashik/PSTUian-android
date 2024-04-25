package com.workfort.pstuian.app.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.repository.FacultyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentsViewModel(
    private val batchId: Int,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: StudentsScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<StudentsScreenState> get() = _screenState

    private fun updateScreenState(update: StudentsScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(StudentsScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(StudentsScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        StudentsScreenStateUpdate.NavigateTo(StudentsScreenState.NavigationState.GoBack)
    )

    fun onClickStudent(student: StudentEntity) = updateScreenState(
        StudentsScreenStateUpdate.NavigateTo(
            StudentsScreenState.NavigationState.GoToStudentProfile(student)
        )
    )

    fun onClickCall(phoneNumber: String) = updateScreenState(
        StudentsScreenStateUpdate.UpdateMessageState(
            StudentsScreenState.DisplayState.MessageState.Call(phoneNumber)
        )
    )

    fun loadStudentList() {
        viewModelScope.launch {
            runCatching {
                val batch = facultyRepo.getBatch(batchId)
                updateScreenState(
                    StudentsScreenStateUpdate.UpdateTitle(batch.title ?: batch.name)
                )
                getStudents(batch.facultyId, batchId)
            }.onFailure {
                updateScreenState(StudentsScreenStateUpdate.UpdateTitle("Batch"))
            }
        }
    }

    private val studentListCache = arrayListOf<StudentEntity>()
    private var hasMoreData = true
    private fun getStudents(facultyId: Int, batchId: Int) {
        if (hasMoreData.not()) return
        viewModelScope.launch {
            updateScreenState(
                StudentsScreenStateUpdate.UpdateStudentListData(
                    students = studentListCache,
                    isLoading = true,
                )
            )
            runCatching {
                val students = facultyRepo.getStudents(facultyId, batchId, forceRefresh = true)
                studentListCache.clear()
                studentListCache.addAll(students)
                updateScreenState(
                    StudentsScreenStateUpdate.UpdateStudentListData(
                        students = studentListCache,
                        isLoading = false,
                    )
                )
            }.onFailure {
                val message = it.message ?: "Failed to load students"
                hasMoreData = false
                if (studentListCache.isEmpty()) {
                    updateScreenState(
                        StudentsScreenStateUpdate.StudentListLoadFailed(message)
                    )
                } else {
                    updateScreenState(
                        StudentsScreenStateUpdate.UpdateStudentListData(
                            students = studentListCache,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }
}