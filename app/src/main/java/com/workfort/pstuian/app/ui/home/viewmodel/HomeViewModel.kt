package com.workfort.pstuian.app.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.home.intent.HomeIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.SliderRepository
import com.workfort.pstuian.sharedpref.Prefs
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authRepo: AuthRepository,
    private val sliderRepo: SliderRepository,
    private val facultyRepo: FacultyRepository
) : ViewModel() {
    val intent = Channel<HomeIntent>(Channel.UNLIMITED)

    var sliderStateForceRefresh = false
    private val _sliderState = MutableStateFlow<RequestState>(RequestState.Idle)
    val sliderState: StateFlow<RequestState> get() = _sliderState

    private val _deleteAllDataState = MutableStateFlow<RequestState>(RequestState.Idle)
    val deleteAllDataState: StateFlow<RequestState> get() = _deleteAllDataState

    private val _clearCache = MutableStateFlow<RequestState>(RequestState.Idle)
    val clearCache: StateFlow<RequestState> get() = _clearCache

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

    private fun getSliders() {
        viewModelScope.launch {
            _sliderState.value = RequestState.Loading
            _sliderState.value = try {
                RequestState.Success(sliderRepo.getSliders(sliderStateForceRefresh))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun clearAllData() {
        viewModelScope.launch {
            _deleteAllDataState.value = RequestState.Loading
            _deleteAllDataState.value = try {
                /** even if we clear the data the device id must not be removed.
                *   because device id should be generated only once during one installation.
                *   so we ensure the device id.
                */
                val deviceId = Prefs.deviceId
                Prefs.clear()
                Prefs.deviceId = deviceId

                authRepo.deleteAll()
                sliderRepo.deleteAll()
                facultyRepo.deleteAll()
                authRepo.updateDataRefreshState()
                RequestState.Success<Unit>()
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    /**
     * Clear stored slider, faculty, batch, students, teachers, courses and employees
     * */
    private fun clearCache() {
        viewModelScope.launch {
            _clearCache.value = RequestState.Loading
            _clearCache.value = try {
                sliderRepo.deleteAll()
                facultyRepo.deleteAll()
                RequestState.Success<Unit>()
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }
}
