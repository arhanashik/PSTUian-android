package com.workfort.pstuian.ui.faculty

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.BatchEntity
import com.workfort.pstuian.featuredomain.model.CourseEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.state.FacultyMessageState
import com.workfort.pstuian.ui.faculty.state.FacultyNavigationState
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FacultyViewModel(
    private val currentFacultyId: Int,
    private val facultyRepo: FacultyRepositoryImpl,
    private val uiStateMachine: FacultyUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<FacultyUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<FacultyMessageState?>(null)
    val message: StateFlow<FacultyMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<FacultyNavigationState?>(null)
    val navigation: StateFlow<FacultyNavigationState?> = _navigation.asStateFlow()

    private val teacherListCache = arrayListOf<User.Teacher>()
    private val batchListCache = arrayListOf<BatchEntity>()
    private val courseListCache = arrayListOf<CourseEntity>()
    private val employeeListCache = arrayListOf<User.Employee>()

    override fun onUiReady() {
        setInitialContent()
        getBatches(currentFacultyId)
        getTeachers(currentFacultyId)
        getCourses(currentFacultyId)
        getEmployees(currentFacultyId)
    }

    fun onUiEvent(event: FacultyUiEvent) {
        when (event) {
            is FacultyUiEvent.BackClicked -> onClickBack()
            is FacultyUiEvent.SelectTab -> uiStateMachine.selectTab(event.index)
            is FacultyUiEvent.BatchClicked -> onClickBatch(event.batch)
            is FacultyUiEvent.TeacherClicked -> onClickTeacher(event.teacher)
            is FacultyUiEvent.CourseClicked -> Unit
            is FacultyUiEvent.EmployeeClicked -> onClickEmployee(event.employee)
            is FacultyUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationConsumed() = _navigation.update { null }

    private fun onClickBack() = _navigation.update { FacultyNavigationState.GoBack }

    private fun onClickBatch(batch: BatchEntity) {
        _navigation.update { FacultyNavigationState.GoToStudentsScreen(batch.id) }
    }

    private fun onClickTeacher(teacher: User.Teacher) {
        _navigation.update { FacultyNavigationState.GoToTeacherScreen(teacher.userId) }
    }

    private fun onClickEmployee(employee: User.Employee) {
        _navigation.update { FacultyNavigationState.GoToEmployeeScreen(employee.userId) }
    }

    private fun onClickCall(phoneNumber: String) {
        _message.update {
            FacultyMessageState.ConfirmCall(phoneNumber) {
                // Do calling
            }
        }
    }

    private fun setInitialContent() {
        viewModelScope.launch {
            runCatching {
                facultyRepo.getFaculty(currentFacultyId).title
            }.onSuccess { title ->
                uiStateMachine.setInitialContent(
                    title = title,
                    tabs = listOf("Batch", "Teacher", "Course", "Employee"),
                    selectedTab = 0,
                )
            }.onFailure {
                onClickBack()
            }
        }
    }

    private fun getBatches(facultyId: Int) {
        viewModelScope.launch {
            uiStateMachine.updateBatchList(batches = batchListCache, isLoading = true)
            runCatching {
                facultyRepo.getBatches(facultyId)
            }.onSuccess {
                batchListCache.clear()
                batchListCache.addAll(it)
                uiStateMachine.updateBatchList(batches = batchListCache, isLoading = false)
            }.onFailure {
                val message = it.message ?: "Failed to load batches"
                if (batchListCache.isEmpty()) {
                    uiStateMachine.updateBatchList(batches = emptyList(), isLoading = false, error = message)
                } else {
                    uiStateMachine.updateBatchList(batches = batchListCache, isLoading = false)
                }
            }
        }
    }

    private fun getTeachers(facultyId: Int) {
        viewModelScope.launch {
            uiStateMachine.updateTeacherList(teachers = teacherListCache, isLoading = true)
            runCatching {
                facultyRepo.getTeachers(facultyId, forceRefresh = true)
            }.onSuccess {
                teacherListCache.clear()
                teacherListCache.addAll(it)
                uiStateMachine.updateTeacherList(teachers = teacherListCache, isLoading = false)
            }.onFailure {
                val message = it.message ?: "Failed to load teachers"
                if (teacherListCache.isEmpty()) {
                    uiStateMachine.updateTeacherList(teachers = emptyList(), isLoading = false, error = message)
                } else {
                    uiStateMachine.updateTeacherList(teachers = teacherListCache, isLoading = false)
                }
            }
        }
    }

    private fun getCourses(facultyId: Int) {
        viewModelScope.launch {
            uiStateMachine.updateCourseList(courses = courseListCache, isLoading = true)
            runCatching {
                facultyRepo.getCourses(facultyId)
            }.onSuccess {
                courseListCache.clear()
                courseListCache.addAll(it)
                uiStateMachine.updateCourseList(courses = courseListCache, isLoading = false)
            }.onFailure {
                val message = it.message ?: "Failed to load courses"
                if (courseListCache.isEmpty()) {
                    uiStateMachine.updateCourseList(courses = emptyList(), isLoading = false, error = message)
                } else {
                    uiStateMachine.updateCourseList(courses = courseListCache, isLoading = false)
                }
            }
        }
    }

    private fun getEmployees(facultyId: Int) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateEmployeeList(employees = employeeListCache, isLoading = true)
            runCatching {
                facultyRepo.getEmployees(facultyId)
            }.onSuccess {
                employeeListCache.clear()
                employeeListCache.addAll(it)
                uiStateMachine.updateEmployeeList(employees = employeeListCache, isLoading = false)
            }.onFailure {
                val message = it.message ?: "Failed to load employees"
                if (employeeListCache.isEmpty()) {
                    uiStateMachine.updateEmployeeList(employees = emptyList(), isLoading = false, error = message)
                } else {
                    uiStateMachine.updateEmployeeList(employees = employeeListCache, isLoading = false)
                }
            }
        }
    }
}
