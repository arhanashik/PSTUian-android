package com.workfort.pstuian.app.ui.faculty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.faculty.intent.FacultyIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.FacultyRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FacultyViewModel(private val facultyRepo: FacultyRepository) : ViewModel() {
    val intent = Channel<FacultyIntent>(Channel.UNLIMITED)

    var facultyId: Int? = null
    var facultyStateForceRefresh = false
    private val _facultyState = MutableStateFlow<RequestState>(RequestState.Idle)
    val facultyState: StateFlow<RequestState> get() = _facultyState

    var batchesStateForceRefresh = false
    private val _batchesState = MutableStateFlow<RequestState>(RequestState.Idle)
    val batchesState: StateFlow<RequestState> get() = _batchesState

    var batchId: Int? = null
    var studentsStateForceRefresh = false
    private val _studentsState = MutableStateFlow<RequestState>(RequestState.Idle)
    val studentsState: StateFlow<RequestState> get() = _studentsState

    var teacherStateForceRefresh = false
    private val _teacherState = MutableStateFlow<RequestState>(RequestState.Idle)
    val teacherState: StateFlow<RequestState> get() = _teacherState

    var courseStateForceRefresh = false
    private val _courseState = MutableStateFlow<RequestState>(RequestState.Idle)
    val courseState: StateFlow<RequestState> get() = _courseState

    var employeeStateForceRefresh = false
    private val _employeeState = MutableStateFlow<RequestState>(RequestState.Idle)
    val employeeState: StateFlow<RequestState> get() = _employeeState

    private val _batchState = MutableStateFlow<RequestState>(RequestState.Idle)
    val batchState: StateFlow<RequestState> get() = _batchState

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
            _facultyState.value = RequestState.Loading
            _facultyState.value = try {
                RequestState.Success(facultyRepo.getFaculties(facultyStateForceRefresh))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    fun getBatch(batchId: Int) {
        viewModelScope.launch {
            _batchState.value = RequestState.Loading
            _batchState.value = try {
                RequestState.Success(facultyRepo.getBatch(batchId))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getBatches(facultyId: Int) {
        viewModelScope.launch {
            _batchesState.value = RequestState.Loading
            _batchesState.value = try {
                RequestState.Success(facultyRepo.getBatches(facultyId, batchesStateForceRefresh))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getStudents(facultyId: Int, batchId: Int) {
        viewModelScope.launch {
            _studentsState.value = RequestState.Loading
            _studentsState.value = try {
                RequestState.Success(
                    facultyRepo.getStudents(facultyId, batchId, studentsStateForceRefresh)
                )
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getTeachers(facultyId: Int) {
        viewModelScope.launch {
            _teacherState.value = RequestState.Loading
            _teacherState.value = try {
                RequestState.Success(facultyRepo.getTeachers(facultyId, teacherStateForceRefresh))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getCourses(facultyId: Int) {
        viewModelScope.launch {
            _courseState.value = RequestState.Loading
            _courseState.value = try {
                RequestState.Success(facultyRepo.getCourses(facultyId, courseStateForceRefresh))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getEmployees(facultyId: Int) {
        viewModelScope.launch {
            _employeeState.value = RequestState.Loading
            _employeeState.value = try {
                RequestState.Success(facultyRepo.getEmployees(facultyId, employeeStateForceRefresh))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}
