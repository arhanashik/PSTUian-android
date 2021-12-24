package com.workfort.pstuian.app.ui.common.locationpicker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.CheckInLocationRepository
import com.workfort.pstuian.app.ui.common.locationpicker.intent.CheckInLocationIntent
import com.workfort.pstuian.app.ui.common.locationpicker.viewstate.CheckInLocationListState
import com.workfort.pstuian.app.ui.common.locationpicker.viewstate.CheckInLocationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 22:30.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInLocationViewModel(
    private val checkInLocationRepo: CheckInLocationRepository
) : ViewModel() {
    val intent = Channel<CheckInLocationIntent>(Channel.UNLIMITED)

    val checkInLocationSearchQuery = MutableStateFlow("")

    var checkInLocationPage = 1
    private val _checkInLocationListState = MutableStateFlow<CheckInLocationListState>(
        CheckInLocationListState.Idle)
    val checkInLocationListState: StateFlow<CheckInLocationListState> get() =
        _checkInLocationListState

    private val _checkInLocationState = MutableStateFlow<CheckInLocationState>(
        CheckInLocationState.Idle)
    val checkInLocationState: StateFlow<CheckInLocationState> get() = _checkInLocationState

    private val _newCheckInLocationState = MutableStateFlow<CheckInLocationState>(
        CheckInLocationState.Idle)
    val newCheckInLocationState: StateFlow<CheckInLocationState> get() = _newCheckInLocationState

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
            _checkInLocationListState.value = CheckInLocationListState.Loading
            _checkInLocationListState.value = try {
                CheckInLocationListState.Success(checkInLocationRepo.search(query, page))
            } catch (ex: Exception) {
                CheckInLocationListState.Error(ex.message?: "Failed to search")
            }
        }
    }

    private fun get(id: Int) {
        viewModelScope.launch {
            _checkInLocationState.value = CheckInLocationState.Loading
            _checkInLocationState.value = try {
                CheckInLocationState.Success(checkInLocationRepo.get(id))
            } catch (ex: Exception) {
                CheckInLocationState.Error(ex.message?: "Failed to search")
            }
        }
    }

    private fun createNewLocation(name: String) {
        viewModelScope.launch {
            _newCheckInLocationState.value = CheckInLocationState.Loading
            _newCheckInLocationState.value = try {
                CheckInLocationState.Success(checkInLocationRepo.insert(name))
            } catch (ex: Exception) {
                CheckInLocationState.Error(ex.message?: "Failed to create!")
            }
        }
    }

    // clear the state values
    fun resetSearchQueryAndNewLocation() {
        checkInLocationSearchQuery.value = ""
        _newCheckInLocationState.value = CheckInLocationState.Idle
    }
}