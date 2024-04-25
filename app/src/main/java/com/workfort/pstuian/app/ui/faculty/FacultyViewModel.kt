package com.workfort.pstuian.app.ui.faculty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.model.BatchEntity
import com.workfort.pstuian.model.CourseEntity
import com.workfort.pstuian.model.EmployeeEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.repository.FacultyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FacultyViewModel(
    private val currentFacultyId: Int,
    private val facultyRepo: FacultyRepository,
    private val stateReducer: FacultyScreenStateReducer,
) : ViewModel() {

    private val _screenState = MutableStateFlow(stateReducer.initial)
    val screenState: StateFlow<FacultyScreenState> get() = _screenState

    private fun updateScreenState(update: FacultyScreenStateUpdate) =
        _screenState.update { oldState -> stateReducer.reduce(oldState, update) }

    fun messageConsumed() = updateScreenState(FacultyScreenStateUpdate.MessageConsumed)

    fun navigationConsumed() = updateScreenState(FacultyScreenStateUpdate.NavigationConsumed)

    fun onClickBack() = updateScreenState(
        FacultyScreenStateUpdate.NavigateTo(FacultyScreenState.NavigationState.GoBack)
    )

    fun onClickBatch(batch: BatchEntity) = updateScreenState(
        FacultyScreenStateUpdate.NavigateTo(
            FacultyScreenState.NavigationState.GoToStudentsScreen(batch)
        )
    )

    fun onClickTeacher(teacher: TeacherEntity) = updateScreenState(
        FacultyScreenStateUpdate.NavigateTo(
            FacultyScreenState.NavigationState.GoToTeacherScreen(teacher)
        )
    )

    fun onClickEmployee(employee: EmployeeEntity) = updateScreenState(
        FacultyScreenStateUpdate.NavigateTo(
            FacultyScreenState.NavigationState.GoToEmployeeScreen(employee)
        )
    )

    fun onClickCall(phoneNumber: String) = updateScreenState(
        FacultyScreenStateUpdate.UpdateMessageState(
            FacultyScreenState.DisplayState.MessageState.Call(phoneNumber)
        )
    )

    fun loadInitialData() {
        getFacultyTitle()
        getBatches(currentFacultyId)
        getTeachers(currentFacultyId)
        getCourses(currentFacultyId)
        getEmployees(currentFacultyId)
    }

    private fun getFacultyTitle() {
        viewModelScope.launch {
            runCatching {
                val title = facultyRepo.getFaculty(currentFacultyId).title
                updateScreenState(FacultyScreenStateUpdate.UpdateTitle(title))
            }.onFailure {
                updateScreenState(FacultyScreenStateUpdate.UpdateTitle("Faculty"))
            }
        }
    }

    private val batchListCache = arrayListOf<BatchEntity>()
    private fun getBatches(facultyId: Int) {
        viewModelScope.launch {
            updateScreenState(
                FacultyScreenStateUpdate.UpdateBatchListData(
                    batches = batchListCache,
                    isLoading = true,
                )
            )
            runCatching {
                facultyRepo.getBatches(facultyId)
            }.onSuccess {
                batchListCache.clear()
                batchListCache.addAll(it)
                updateScreenState(
                    FacultyScreenStateUpdate.UpdateBatchListData(
                        batches = batchListCache,
                        isLoading = false,
                    )
                )
            }.onFailure {
                val message = it.message ?: "Failed to load batches"
                if (batchListCache.isEmpty()) {
                    updateScreenState(FacultyScreenStateUpdate.LoadBatchListFailed(message))
                } else {
                    updateScreenState(
                        FacultyScreenStateUpdate.UpdateBatchListData(
                            batches = batchListCache,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }

    private val teacherListCache = arrayListOf<TeacherEntity>()
    private fun getTeachers(facultyId: Int) {
        viewModelScope.launch {
            updateScreenState(
                FacultyScreenStateUpdate.UpdateTeacherListData(
                    teachers = teacherListCache,
                    isLoading = true,
                )
            )
            runCatching {
                facultyRepo.getTeachers(facultyId, forceRefresh = true)
            }.onSuccess {
                teacherListCache.clear()
                teacherListCache.addAll(it)
                updateScreenState(
                    FacultyScreenStateUpdate.UpdateTeacherListData(
                        teachers = teacherListCache,
                        isLoading = false,
                    )
                )
            }.onFailure {
                val message = it.message ?: "Failed to load teachers"
                if (teacherListCache.isEmpty()) {
                    updateScreenState(FacultyScreenStateUpdate.LoadTeacherListFailed(message))
                } else {
                    updateScreenState(
                        FacultyScreenStateUpdate.UpdateTeacherListData(
                            teachers = teacherListCache,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }

    private val courseListCache = arrayListOf<CourseEntity>()
    private fun getCourses(facultyId: Int) {
        viewModelScope.launch {
            updateScreenState(
                FacultyScreenStateUpdate.UpdateCourseListData(
                    courses = courseListCache,
                    isLoading = true,
                )
            )
            runCatching {
                facultyRepo.getCourses(facultyId)
            }.onSuccess {
                courseListCache.clear()
                courseListCache.addAll(it)
                updateScreenState(
                    FacultyScreenStateUpdate.UpdateCourseListData(
                        courses = courseListCache,
                        isLoading = false,
                    )
                )
            }.onFailure {
                val message = it.message ?: "Failed to load courses"
                if (courseListCache.isEmpty()) {
                    updateScreenState(FacultyScreenStateUpdate.LoadCourseListFailed(message))
                } else {
                    updateScreenState(
                        FacultyScreenStateUpdate.UpdateCourseListData(
                            courses = courseListCache,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }

    private val employeeListCache = arrayListOf<EmployeeEntity>()
    private fun getEmployees(facultyId: Int) {
        viewModelScope.launch {
            updateScreenState(
                FacultyScreenStateUpdate.UpdateEmployeeListData(
                    employees = employeeListCache,
                    isLoading = true,
                )
            )
            runCatching {
                facultyRepo.getEmployees(facultyId)
            }.onSuccess {
                employeeListCache.clear()
                employeeListCache.addAll(it)
                updateScreenState(
                    FacultyScreenStateUpdate.UpdateEmployeeListData(
                        employees = employeeListCache,
                        isLoading = false,
                    ),
                )
            }.onFailure {
                val message = it.message ?: "Failed to load employees"
                if (courseListCache.isEmpty()) {
                    updateScreenState(FacultyScreenStateUpdate.LoadEmployeeListFailed(message))
                } else {
                    updateScreenState(
                        FacultyScreenStateUpdate.UpdateEmployeeListData(
                            employees = employeeListCache,
                            isLoading = false,
                        )
                    )
                }
            }
        }
    }
}
