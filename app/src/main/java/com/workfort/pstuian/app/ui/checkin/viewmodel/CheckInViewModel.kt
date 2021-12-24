package com.workfort.pstuian.app.ui.checkin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.CheckInRepository
import com.workfort.pstuian.app.ui.checkin.intent.CheckInIntent
import com.workfort.pstuian.app.ui.checkin.viewstate.CheckInDeleteState
import com.workfort.pstuian.app.ui.checkin.viewstate.CheckInListState
import com.workfort.pstuian.app.ui.checkin.viewstate.CheckInState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:45.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/13.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class CheckInViewModel(
    private val checkInRepo : CheckInRepository
) : ViewModel() {
    val intent = Channel<CheckInIntent>(Channel.UNLIMITED)

    var checkInListPage = 1
    private val _checkInListState = MutableStateFlow<CheckInListState>(CheckInListState.Idle)
    val checkInListState: StateFlow<CheckInListState> get() = _checkInListState

    private val _checkInState = MutableStateFlow<CheckInState>(CheckInState.Idle)
    val checkInState: StateFlow<CheckInState> get() = _checkInState

    var userCheckInListPage = 1
    private val _userCheckInListState = MutableStateFlow<CheckInListState>(CheckInListState.Idle)
    val userCheckInListState: StateFlow<CheckInListState> get() = _userCheckInListState

    private val _checkInPrivacyState = MutableStateFlow<CheckInState>(CheckInState.Idle)
    val checkInPrivacyState: StateFlow<CheckInState> get() = _checkInPrivacyState

    private val _checkInDeleteState = MutableStateFlow<CheckInDeleteState>(CheckInDeleteState.Idle)
    val checkInDeleteState: StateFlow<CheckInDeleteState> get() = _checkInDeleteState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is CheckInIntent.GetAll -> getCheckInList(it.locationId, it.page)
                    is CheckInIntent.GetAllByUser -> getCheckInList(it.userId, it.userType, it.page)
                    is CheckInIntent.CheckIn -> checkIn(it.locationId)
                    is CheckInIntent.UpdatePrivacy -> updatePrivacy(it.checkInId, it.privacy)
                    is CheckInIntent.Delete -> delete(it.checkInId)
                }
            }
        }
    }

    private fun getCheckInList(locationId: Int, page: Int) {
        viewModelScope.launch {
            _checkInListState.value = CheckInListState.Loading
            _checkInListState.value = try {
                CheckInListState.CheckInList(checkInRepo.getAll(locationId, page))
            } catch (e: Exception) {
                CheckInListState.Error(e.message)
            }
        }
    }

    private fun getCheckInList(userId: Int, userType: String, page: Int) {
        viewModelScope.launch {
            _userCheckInListState.value = CheckInListState.Loading
            _userCheckInListState.value = try {
                CheckInListState.CheckInList(checkInRepo.getAll(userId, userType, page))
            } catch (e: Exception) {
                CheckInListState.Error(e.message)
            }
        }
    }

    private fun checkIn(locationId: Int) {
        viewModelScope.launch {
            _checkInState.value = CheckInState.Loading
            _checkInState.value = try {
                CheckInState.Success(checkInRepo.checkIn(locationId))
            } catch (e: Exception) {
                CheckInState.Error(e.message?: "Check in failed")
            }
        }
    }

    private fun updatePrivacy(checkInId: Int, privacy: String) {
        viewModelScope.launch {
            _checkInPrivacyState.value = CheckInState.Loading
            _checkInPrivacyState.value = try {
                val response = checkInRepo.updatePrivacy(checkInId, privacy)
                CheckInState.Success(response)
            } catch (e: Exception) {
                CheckInState.Error(e.message?: "Update failed")
            }
        }
    }

    private fun delete(checkInId: Int) {
        viewModelScope.launch {
            _checkInDeleteState.value = CheckInDeleteState.Loading
            _checkInDeleteState.value = try {
                if(checkInRepo.delete(checkInId)) {
                    CheckInDeleteState.Success(checkInId)
                } else {
                    CheckInDeleteState.Error("Failed to delete")
                }
            } catch (e: Exception) {
                CheckInDeleteState.Error(e.message?: "Failed to delete")
            }
        }
    }
}