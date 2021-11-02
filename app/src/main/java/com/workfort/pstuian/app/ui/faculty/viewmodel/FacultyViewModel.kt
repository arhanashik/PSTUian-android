package com.workfort.pstuian.app.ui.faculty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.FacultyRepository
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.app.ui.faculty.viewstate.*
import com.workfort.pstuian.app.ui.students.viewstate.StudentsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FacultyViewModel(private val facultyRepo: FacultyRepository) : ViewModel() {
    val intent = Channel<FacultyIntent>(Channel.UNLIMITED)

    var facultyId: Int? = null
    var facultyStateForceRefresh = false
    private val _facultyState = MutableStateFlow<FacultyState>(FacultyState.Idle)
    val facultyState: StateFlow<FacultyState> get() = _facultyState

    var batchesStateForceRefresh = false
    private val _batchesState = MutableStateFlow<BatchesState>(BatchesState.Idle)
    val batchesState: StateFlow<BatchesState> get() = _batchesState

    var batchId: Int? = null
    var studentsStateForceRefresh = false
    private val _studentsState = MutableStateFlow<StudentsState>(StudentsState.Idle)
    val studentsState: StateFlow<StudentsState> get() = _studentsState

    var teacherStateForceRefresh = false
    private val _teacherState = MutableStateFlow<TeacherState>(TeacherState.Idle)
    val teacherState: StateFlow<TeacherState> get() = _teacherState

    var courseStateForceRefresh = false
    private val _courseState = MutableStateFlow<CourseState>(CourseState.Idle)
    val courseState: StateFlow<CourseState> get() = _courseState

    var employeeStateForceRefresh = false
    private val _employeeState = MutableStateFlow<EmployeeState>(EmployeeState.Idle)
    val employeeState: StateFlow<EmployeeState> get() = _employeeState

    private val _batchState = MutableStateFlow<BatchState>(BatchState.Idle)
    val batchState: StateFlow<BatchState> get() = _batchState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is FacultyIntent.GetFaculties -> getFaculties()
                    is FacultyIntent.GetBatches -> facultyId?.let { getBatches(it) }
                    is FacultyIntent.GetStudents -> {
                        if(facultyId != null && batchId != null) {
                            getStudents(facultyId!!, batchId!!)
                        }
                    }
                    is FacultyIntent.GetTeachers -> facultyId?.let { getTeachers(it) }
                    is FacultyIntent.GetCourses -> facultyId?.let { getCourses(it) }
                    is FacultyIntent.GetEmployees -> facultyId?.let { getEmployees(it) }
                }
            }
        }
    }

    private fun getFaculties() {
        viewModelScope.launch {
            _facultyState.value = FacultyState.Loading
            _facultyState.value = try {
                FacultyState.Faculties(facultyRepo.getFaculties(facultyStateForceRefresh))
            } catch (e: Exception) {
                FacultyState.Error(e.message)
            }
        }
    }

    fun getBatch(batchId: Int) {
        viewModelScope.launch {
            _batchState.value = BatchState.Loading
            _batchState.value = try {
                BatchState.Batch(facultyRepo.getBatch(batchId))
            } catch (e: Exception) {
                BatchState.Error(e.message)
            }
        }
    }

    private fun getBatches(facultyId: Int) {
        viewModelScope.launch {
            _batchesState.value = BatchesState.Loading
            _batchesState.value = try {
                BatchesState.Batches(facultyRepo.getBatches(
                    facultyId, batchesStateForceRefresh))
            } catch (e: Exception) {
                BatchesState.Error(e.message)
            }
        }
    }

    private fun getStudents(facultyId: Int, batchId: Int) {
        viewModelScope.launch {
            _studentsState.value = StudentsState.Loading
            _studentsState.value = try {
                StudentsState.Students(facultyRepo.getStudents(facultyId, batchId,
                    studentsStateForceRefresh))
            } catch (e: Exception) {
                StudentsState.Error(e.message)
            }
        }
    }

    private fun getTeachers(facultyId: Int) {
        viewModelScope.launch {
            _teacherState.value = TeacherState.Loading
            _teacherState.value = try {
                TeacherState.Teachers(facultyRepo.getTeachers(facultyId, teacherStateForceRefresh))
            } catch (e: Exception) {
                TeacherState.Error(e.message)
            }
        }
    }

    private fun getCourses(facultyId: Int) {
        viewModelScope.launch {
            _courseState.value = CourseState.Loading
            _courseState.value = try {
                CourseState.Courses(facultyRepo.getCourses(facultyId, courseStateForceRefresh))
            } catch (e: Exception) {
                CourseState.Error(e.message)
            }
        }
    }

    private fun getEmployees(facultyId: Int) {
        viewModelScope.launch {
            _employeeState.value = EmployeeState.Loading
            _employeeState.value = try {
                EmployeeState.Employees(facultyRepo.getEmployees(
                    facultyId, employeeStateForceRefresh))
            } catch (e: Exception) {
                EmployeeState.Error(e.message)
            }
        }
    }
}
