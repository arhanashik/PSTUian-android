package com.workfort.pstuian.app.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.repository.DonationRepository
import com.workfort.pstuian.app.data.repository.FacultyRepository
import com.workfort.pstuian.app.data.repository.SliderRepository
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewstate.FacultyState
import com.workfort.pstuian.app.ui.home.viewstate.SliderState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val sliderRepo: SliderRepository,
    private val facultyRepo: FacultyRepository
) : ViewModel() {
    val intent = Channel<HomeIntent>(Channel.UNLIMITED)
    private val _sliderState = MutableStateFlow<SliderState>(SliderState.Idle)
    val sliderState: StateFlow<SliderState> get() = _sliderState

    private val _facultyState = MutableStateFlow<FacultyState>(FacultyState.Idle)
    val facultyState: StateFlow<FacultyState> get() = _facultyState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.GetSliders -> getSliders()
                    is HomeIntent.GetFaculties -> getFaculties()
                }
            }
        }
    }

    private fun getSliders()
    {
        viewModelScope.launch {
            _sliderState.value = SliderState.Loading
            _sliderState.value = try {
                SliderState.Sliders(sliderRepo.getSliders())
            } catch (e: Exception) {
                SliderState.Error(e.message)
            }
        }
    }

    private fun getFaculties() {
        viewModelScope.launch {
            _facultyState.value = FacultyState.Loading
            _facultyState.value = try {
                FacultyState.Faculties(facultyRepo.getFaculties())
            } catch (e: Exception) {
                FacultyState.Error(e.message)
            }
        }
    }

    fun clearAllData(): Boolean {
        thread(start = true) {
            Prefs.clear()
//            DatabaseHelper.provideSliderService().deleteAll()
//            DatabaseHelper.provideFacultyService().deleteAll()
//            DatabaseHelper.provideTeacherService().deleteAll()
//            DatabaseHelper.provideStudentService().deleteAll()
//            DatabaseHelper.provideBatchService().deleteAll()
//            DatabaseHelper.provideCourseScheduleService().deleteAll()
//            DatabaseHelper.provideEmployeeService().deleteAll()
        }.join()

        return true
    }
}
