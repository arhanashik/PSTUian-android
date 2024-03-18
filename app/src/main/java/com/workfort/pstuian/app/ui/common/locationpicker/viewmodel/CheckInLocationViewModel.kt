package com.workfort.pstuian.app.ui.common.locationpicker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.common.locationpicker.intent.CheckInLocationIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.CheckInLocationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CheckInLocationViewModel(
    private val checkInLocationRepo: CheckInLocationRepository
) : ViewModel() {
    val intent = Channel<CheckInLocationIntent>(Channel.UNLIMITED)

    val checkInLocationSearchQuery = MutableStateFlow("")

    var checkInLocationPage = 1
    private val _checkInLocationListState = MutableStateFlow<RequestState>(RequestState.Idle)
    val checkInLocationListState: StateFlow<RequestState> get() = _checkInLocationListState

    private val _checkInLocationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val checkInLocationState: StateFlow<RequestState> get() = _checkInLocationState

    private val _newCheckInLocationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val newCheckInLocationState: StateFlow<RequestState> get() = _newCheckInLocationState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when(it) {
                    is CheckInLocationIntent.Search -> search(it.query, it.page)
                    is CheckInLocationIntent.Get -> get(it.id)
                    is CheckInLocationIntent.Create -> createNewLocation(it.name)
                }
            }
        }
    }

    private fun search(query: String, page: Int) {
        viewModelScope.launch {
            _checkInLocationListState.value = RequestState.Loading
            _checkInLocationListState.value = try {
                RequestState.Success(checkInLocationRepo.search(query, page))
            } catch (ex: Exception) {
                RequestState.Error(ex.message?: "Failed to search")
            }
        }
    }

    private fun get(id: Int) {
        viewModelScope.launch {
            _checkInLocationState.value = RequestState.Loading
            _checkInLocationState.value = try {
                RequestState.Success(checkInLocationRepo.get(id))
            } catch (ex: Exception) {
                RequestState.Error(ex.message?: "Failed to search")
            }
        }
    }

    private fun createNewLocation(name: String) {
        viewModelScope.launch {
            _newCheckInLocationState.value = RequestState.Loading
            _newCheckInLocationState.value = try {
                RequestState.Success(checkInLocationRepo.insert(name))
            } catch (ex: Exception) {
                RequestState.Error(ex.message?: "Failed to create!")
            }
        }
    }

    // clear the state values
    fun resetSearchQueryAndNewLocation() {
        checkInLocationSearchQuery.value = ""
        _newCheckInLocationState.value = RequestState.Idle
    }
}