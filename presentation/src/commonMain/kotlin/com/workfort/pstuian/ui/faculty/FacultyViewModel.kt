package com.workfort.pstuian.ui.faculty

import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.data.infrastructure.repository.FacultyRepositoryImpl
import com.workfort.pstuian.featuredomain.framework.coroutine.CoroutineDispatcherProvider
import com.workfort.pstuian.featuredomain.framework.coroutine.launchOnMain
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

    private var currentTeacherDataPage = 1
    private var hasMoreTeacherData = true
    private val teacherListCache = arrayListOf<User.Teacher>()

    private var currentCourseDataPage = 1
    private var hasMoreCourseData = true
    private val courseListCache = arrayListOf<Course>()

    private var currentEmployeeDataPage = 1
    private var hasMoreEmployeeData = true
    private val employeeListCache = arrayListOf<User.Employee>()

    override fun onUiReady() {
        setInitialContent()
    }

    fun onUiEvent(event: FacultyUiEvent) {
        when (event) {
            is FacultyUiEvent.BackClicked -> _navigation.update { FacultyNavigationState.GoBack }
            is FacultyUiEvent.SelectTab -> uiStateMachine.selectTab(event.index)
            is FacultyUiEvent.TeacherClicked -> onClickTeacher(event.teacher)
            is FacultyUiEvent.CourseClicked -> Unit
            is FacultyUiEvent.EmployeeClicked -> onClickEmployee(event.employee)
            is FacultyUiEvent.CallClicked -> onClickCall(event.phoneNumber)
        }
    }

    fun onMessageHandled() = _message.update { null }

    fun onNavigationConsumed() = _navigation.update { null }

    private fun onClickTeacher(teacher: User.Teacher) {
        _navigation.update { FacultyNavigationState.GoToTeacherProfileScreen(teacher.userId) }
    }

    private fun onClickEmployee(employee: User.Employee) {
        _navigation.update { FacultyNavigationState.GoToEmployeeProfileScreen(employee.userId) }
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
                        facultyId = facultyId,
                        title = faculty.title,
                        tabs = listOf("Batch", "Teacher", "Course", "Employee"),
                        selectedTab = 0,
                    )
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

    private fun getTeachers(facultyId: Int, forceRefresh: Boolean = false) {
        if (forceRefresh) {
            teacherListCache.clear()
            currentTeacherDataPage = 1
            hasMoreTeacherData = true
        } else if (!hasMoreTeacherData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateTeacherList(isLoading = true)
            facultyRepo.getTeachers(facultyId, currentTeacherDataPage, forceRefresh)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        hasMoreTeacherData = false
                    } else {
                        currentTeacherDataPage++
                        teacherListCache.addAll(list)
                    }
                    uiStateMachine.updateTeacherList(teachers = teacherListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateTeacherList(error = message)
                }
        }
    }

    private fun getCourses(facultyId: Int, forceRefresh: Boolean = false) {
        if (forceRefresh) {
            courseListCache.clear()
            currentCourseDataPage = 1
            hasMoreCourseData = true
        } else if (!hasMoreCourseData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateCourseList(isLoading = true)
            facultyRepo.getCourses(facultyId, currentCourseDataPage, forceRefresh)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        hasMoreCourseData = false
                    } else {
                        currentCourseDataPage++
                        courseListCache.addAll(list)
                    }
                    uiStateMachine.updateCourseList(courses = courseListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateCourseList(error = message)
                }
        }
    }

    private fun getEmployees(facultyId: Int, forceRefresh: Boolean = false) {
        if (forceRefresh) {
            employeeListCache.clear()
            currentEmployeeDataPage = 1
            hasMoreEmployeeData = true
        } else if (!hasMoreEmployeeData) {
            return
        }

        viewModelScope.launchOnMain(coroutineDispatcherProvider) {
            uiStateMachine.updateEmployeeList(isLoading = true)
            facultyRepo.getEmployees(facultyId, currentEmployeeDataPage, forceRefresh)
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        hasMoreEmployeeData = false
                    } else {
                        currentEmployeeDataPage++
                        employeeListCache.addAll(list)
                    }
                    uiStateMachine.updateEmployeeList(employees = employeeListCache)
                }
                .onFailure {
                    val message = it.message ?: "Failed to load data"
                    uiStateMachine.updateEmployeeList(error = message)
                }
        }
    }
}
