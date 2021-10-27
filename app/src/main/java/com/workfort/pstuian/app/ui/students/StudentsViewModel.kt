package com.workfort.pstuian.app.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.FacultyRepository
import com.workfort.pstuian.app.ui.students.intent.StudentsIntent
import com.workfort.pstuian.app.ui.students.viewstate.StudentsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class StudentsViewModel(private val facultyRepo: FacultyRepository) : ViewModel() {
    val intent = Channel<StudentsIntent>(Channel.UNLIMITED)

    private val _studentsState = MutableStateFlow<StudentsState>(StudentsState.Idle)
    val studentsState: StateFlow<StudentsState> get() = _studentsState

    fun handleIntent(facultyId: Int, batchId: Int) {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is StudentsIntent.GetStudents -> getStudents(facultyId, batchId)
                }
            }
        }
    }

    private fun getStudents(facultyId: Int, batchId: Int) {
        viewModelScope.launch {
            _studentsState.value = StudentsState.Loading
            _studentsState.value = try {
                StudentsState.Students(facultyRepo.getStudents(facultyId, batchId))
            } catch (e: Exception) {
                StudentsState.Error(e.message)
            }
        }
    }
}
