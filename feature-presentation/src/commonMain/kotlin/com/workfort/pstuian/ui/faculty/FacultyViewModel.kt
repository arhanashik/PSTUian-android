package com.workfort.pstuian.ui.faculty

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
import com.workfort.pstuian.featuredomain.model.Batch
import com.workfort.pstuian.featuredomain.model.Course
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.ui.common.uistate.UiStateMachineViewModel
import com.workfort.pstuian.ui.faculty.state.FacultyMessageState
import com.workfort.pstuian.ui.faculty.state.FacultyNavigationState
import com.workfort.pstuian.ui.faculty.state.FacultyUiEvent
import com.workfort.pstuian.ui.faculty.state.FacultyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FacultyViewModel(
    private val facultyId: Int,
    private val facultyRepo: FacultyRepositoryImpl,
    private val uiStateMachine: FacultyUiStateMachine,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : UiStateMachineViewModel<FacultyUiState>(uiStateMachine) {

    private val _message = MutableStateFlow<FacultyMessageState?>(null)
    val message: StateFlow<FacultyMessageState?> = _message.asStateFlow()

    private val _navigation = MutableStateFlow<FacultyNavigationState?>(null)
    val navigation: StateFlow<FacultyNavigationState?> = _navigation.asStateFlow()

    private val batchListCache = arrayListOf<Batch>()
    private val teacherListCache = arrayListOf<User.Teacher>()
    private val courseListCache = arrayListOf<Course>()
    private val employeeListCache = arrayListOf<User.Employee>()

    override fun onUiReady() {
        setInitialContent()
    }

    fun onUiEvent(event: FacultyUiEvent) {
        when (event) {
            is FacultyUiEvent.BackClicked -> _navigation.update { FacultyNavigationState.GoBack }
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

    private fun onClickBatch(batch: Batch) {
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
        uiStateMachine.showLoadingOverlay(true)
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            facultyRepo.getFaculty(facultyId)
                .onSuccess { faculty ->
                    uiStateMachine.setInitialContent(
                        title = faculty.title,
                        tabs = listOf("Batch", "Teacher", "Course", "Employee"),
                        selectedTab = 0,
                    )
                    getBatches(facultyId)
                    getTeachers(facultyId)
                    getCourses(facultyId)
                    getEmployees(facultyId)
                }
                .onFailure { error ->
                    _message.update {
                        FacultyMessageState.ShowError(message = error.message ?: "Failed to load data") {
                            onMessageHandled()
                            setInitialContent()
                        }
                    }
                }
        }
    }

    private fun getBatches(facultyId: Int) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateBatchList(isLoading = true)
            facultyRepo.getBatches(facultyId)
                .onSuccess {
                    batchListCache.clear()
                    batchListCache.addAll(it)
                    uiStateMachine.updateBatchList(batches = batchListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateBatchList(error = message)
                }
        }
    }

    private fun getTeachers(facultyId: Int) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateTeacherList(isLoading = true)
            facultyRepo.getTeachers(facultyId)
                .onSuccess {
                    teacherListCache.clear()
                    teacherListCache.addAll(it)
                    uiStateMachine.updateTeacherList(teachers = teacherListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateTeacherList(error = message)
                }
        }
    }

    private fun getCourses(facultyId: Int) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateCourseList(isLoading = true)
            facultyRepo.getCourses(facultyId)
                .onSuccess {
                    courseListCache.clear()
                    courseListCache.addAll(it)
                    uiStateMachine.updateCourseList(courses = courseListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateCourseList(error = message)
                }
        }
    }

    private fun getEmployees(facultyId: Int) {
        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateEmployeeList(isLoading = true)
            facultyRepo.getEmployees(facultyId)
                .onSuccess {
                    employeeListCache.clear()
                    employeeListCache.addAll(it)
                    uiStateMachine.updateEmployeeList(employees = employeeListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateEmployeeList(error = message)
                }
        }
    }
}
