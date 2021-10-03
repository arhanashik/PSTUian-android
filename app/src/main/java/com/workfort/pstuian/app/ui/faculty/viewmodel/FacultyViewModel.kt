package com.workfort.pstuian.app.ui.faculty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.FacultyRepository
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.viewstate.BatchState
import com.workfort.pstuian.app.ui.faculty.viewstate.CourseState
import com.workfort.pstuian.app.ui.faculty.viewstate.EmployeeState
import com.workfort.pstuian.app.ui.faculty.viewstate.TeacherState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FacultyViewModel(private val facultyRepo: FacultyRepository) : ViewModel() {
    val intent = Channel<FacultyIntent>(Channel.UNLIMITED)

    private val _batchState = MutableStateFlow<BatchState>(BatchState.Idle)
    val batchState: StateFlow<BatchState> get() = _batchState

    private val _teacherState = MutableStateFlow<TeacherState>(TeacherState.Idle)
    val teacherState: StateFlow<TeacherState> get() = _teacherState

    private val _courseState = MutableStateFlow<CourseState>(CourseState.Idle)
    val courseState: StateFlow<CourseState> get() = _courseState

    private val _employeeState = MutableStateFlow<EmployeeState>(EmployeeState.Idle)
    val employeeState: StateFlow<EmployeeState> get() = _employeeState

    fun handleIntent(facultyId: Int) {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is FacultyIntent.GetBatches -> getBatches(facultyId)
                    is FacultyIntent.GetTeachers -> getTeachers(facultyId)
                    is FacultyIntent.GetCourses -> getCourses(facultyId)
                    is FacultyIntent.GetEmployees -> getEmployees(facultyId)
                }
            }
        }
    }

    private fun getBatches(facultyId: Int) {
        viewModelScope.launch {
            _batchState.value = BatchState.Loading
            _batchState.value = try {
                BatchState.Batches(facultyRepo.getBatches(facultyId))
            } catch (e: Exception) {
                BatchState.Error(e.message)
            }
        }
    }

    private fun getTeachers(facultyId: Int) {
        viewModelScope.launch {
            _teacherState.value = TeacherState.Loading
            _teacherState.value = try {
                TeacherState.Teachers(facultyRepo.getTeachers(facultyId))
            } catch (e: Exception) {
                TeacherState.Error(e.message)
            }
        }
    }

    private fun getCourses(facultyId: Int) {
        viewModelScope.launch {
            _courseState.value = CourseState.Loading
            _courseState.value = try {
                CourseState.Courses(facultyRepo.getCourses(facultyId))
            } catch (e: Exception) {
                CourseState.Error(e.message)
            }
        }
    }

    private fun getEmployees(facultyId: Int) {
        viewModelScope.launch {
            _employeeState.value = EmployeeState.Loading
            _employeeState.value = try {
                EmployeeState.Employees(facultyRepo.getEmployees(facultyId))
            } catch (e: Exception) {
                EmployeeState.Error(e.message)
            }
        }
    }
}
