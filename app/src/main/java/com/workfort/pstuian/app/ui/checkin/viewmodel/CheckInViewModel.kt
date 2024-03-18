package com.workfort.pstuian.app.ui.checkin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.checkin.intent.CheckInIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.CheckInRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _checkInListState = MutableStateFlow<RequestState>(RequestState.Idle)
    val checkInListState: StateFlow<RequestState> get() = _checkInListState

    private val _checkInState = MutableStateFlow<RequestState>(RequestState.Idle)
    val checkInState: StateFlow<RequestState> get() = _checkInState

    var userCheckInListPage = 1
    private val _userCheckInListState = MutableStateFlow<RequestState>(RequestState.Idle)
    val userCheckInListState: StateFlow<RequestState> get() = _userCheckInListState

    private val _checkInPrivacyState = MutableStateFlow<RequestState>(RequestState.Idle)
    val checkInPrivacyState: StateFlow<RequestState> get() = _checkInPrivacyState

    private val _checkInDeleteState = MutableStateFlow<RequestState>(RequestState.Idle)
    val checkInDeleteState: StateFlow<RequestState> get() = _checkInDeleteState

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
            _checkInListState.value = RequestState.Loading
            _checkInListState.value = try {
                RequestState.Success(checkInRepo.getAll(locationId, page))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getCheckInList(userId: Int, userType: String, page: Int) {
        viewModelScope.launch {
            _userCheckInListState.value = RequestState.Loading
            _userCheckInListState.value = try {
                RequestState.Success(checkInRepo.getAll(userId, userType, page))
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun checkIn(locationId: Int) {
        viewModelScope.launch {
            _checkInState.value = RequestState.Loading
            _checkInState.value = try {
                RequestState.Success(checkInRepo.checkIn(locationId))
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Check in failed")
            }
        }
    }

    private fun updatePrivacy(checkInId: Int, privacy: String) {
        viewModelScope.launch {
            _checkInPrivacyState.value = RequestState.Loading
            _checkInPrivacyState.value = try {
                val response = checkInRepo.updatePrivacy(checkInId, privacy)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Update failed")
            }
        }
    }

    private fun delete(checkInId: Int) {
        viewModelScope.launch {
            _checkInDeleteState.value = RequestState.Loading
            _checkInDeleteState.value = try {
                if(checkInRepo.delete(checkInId)) {
                    RequestState.Success(checkInId)
                } else {
                    RequestState.Error("Failed to delete")
                }
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to delete")
            }
        }
    }
}