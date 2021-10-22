package com.workfort.pstuian.app.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.repository.AuthRepository
import com.workfort.pstuian.app.data.repository.FacultyRepository
import com.workfort.pstuian.app.data.repository.SliderRepository
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.app.ui.home.viewstate.DeleteAllState
import com.workfort.pstuian.app.ui.home.viewstate.SliderState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepo: AuthRepository,
    private val sliderRepo: SliderRepository,
    private val facultyRepo: FacultyRepository
) : ViewModel() {
    val intent = Channel<HomeIntent>(Channel.UNLIMITED)

    private val _sliderState = MutableStateFlow<SliderState>(SliderState.Idle)
    val sliderState: StateFlow<SliderState> get() = _sliderState

    private val _deleteAllDataState = MutableStateFlow<DeleteAllState>(DeleteAllState.Idle)
    val deleteAllDataState: StateFlow<DeleteAllState> get() = _deleteAllDataState

    private val _clearCache = MutableStateFlow<DeleteAllState>(DeleteAllState.Idle)
    val clearCache: StateFlow<DeleteAllState> get() = _clearCache

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.GetSliders -> getSliders()
                    is HomeIntent.DeleteAllData -> clearAllData()
                    is HomeIntent.ClearCache -> clearCache()
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

    private fun clearAllData() {
        viewModelScope.launch {
            _deleteAllDataState.value = DeleteAllState.Loading
            _deleteAllDataState.value = try {
                Prefs.clear()
                authRepo.deleteAll()
                sliderRepo.deleteAll()
                facultyRepo.deleteAll()
                authRepo.updateDataRefreshState()
                DeleteAllState.Success
            } catch (e: Exception) {
                DeleteAllState.Error(e.message)
            }
        }
    }

    /**
     * Clear stored faculty, batch, students, teachers, courses and employees
     * */
    private fun clearCache() {
        viewModelScope.launch {
            _clearCache.value = DeleteAllState.Loading
            _clearCache.value = try {
                facultyRepo.deleteAll()
                DeleteAllState.Success
            } catch (e: Exception) {
                DeleteAllState.Error(e.message)
            }
        }
    }
}
