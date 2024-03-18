package com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.blooddonationrequest.intent.BloodDonationIntent
import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.BloodDonationRepository
import com.workfort.pstuian.repository.BloodDonationRequestRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 20:30 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class BloodDonationViewModel(
    private val donationRepo: BloodDonationRepository,
    private val donationRequestRepo: BloodDonationRequestRepository,
) : ViewModel() {
    val intent = Channel<BloodDonationIntent>(Channel.UNLIMITED)

    // blood donation states
    var donationsPage = 1
    private val _donationsState = MutableStateFlow<RequestState>(RequestState.Idle)
    val donationsState: StateFlow<RequestState> get() = _donationsState

    private val _createDonationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val createDonationState: StateFlow<RequestState> get() = _createDonationState

    private val _updateDonationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val updateDonationState: StateFlow<RequestState> get() = _updateDonationState

    private val _deleteDonationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val deleteDonationState: StateFlow<RequestState> get() = _deleteDonationState

    // blood donation request states
    var donationRequestsPage = 1
    private val _donationRequestsState = MutableStateFlow<RequestState>(RequestState.Idle)
    val donationRequestsState: StateFlow<RequestState> get() = _donationRequestsState

    private val _createDonationRequestState = MutableStateFlow<RequestState>(RequestState.Idle)
    val createDonationRequestState: StateFlow<RequestState> get() = _createDonationRequestState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is BloodDonationIntent.GetAllDonations ->
                        getDonations(it.userId, it.userType, it.page)
                    is BloodDonationIntent.CreateDonation ->
                        createDonation(it.requestId, it.date, it.info)
                    is BloodDonationIntent.UpdateDonation -> updateDonation(it.item)
                    is BloodDonationIntent.DeleteDonation -> deleteDonation(it.id)

                    is BloodDonationIntent.GetAllDonationRequests -> getDonationRequests(it.page)
                    is BloodDonationIntent.CreateDonationRequest ->
                        createDonationRequest(it.bloodGroup, it.beforeDate, it.contact, it.info)
                }
            }
        }
    }

    private fun getDonations(userId: Int, userType: String, page: Int) {
        viewModelScope.launch {
            _donationsState.value = RequestState.Loading
            _donationsState.value = try {
                RequestState.Success(donationRepo.getAll(userId, userType, page))
            } catch (e: Exception) {
                RequestState.Error(e.message ?: "Failed to get data")
            }
        }
    }

    private fun createDonation(requestId: Int?, date: String, info: String?) {
        _createDonationState.value = RequestState.Loading

        viewModelScope.launch {
            _createDonationState.value = try {
                val response = donationRepo.insert(requestId, date, info)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to create donation")
            }
        }
    }

    private fun updateDonation(item: BloodDonationEntity) {
        viewModelScope.launch {
            _updateDonationState.value = RequestState.Loading
            _updateDonationState.value = try {
                RequestState.Success(donationRepo.update(item))
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to update")
            }
        }
    }

    private fun deleteDonation(id: Int) {
        viewModelScope.launch {
            _deleteDonationState.value = RequestState.Loading
            _deleteDonationState.value = try {
                if(donationRepo.delete(id)) {
                    RequestState.Success(id)
                } else {
                    RequestState.Error("Failed to delete")
                }
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to delete")
            }
        }
    }

    private fun getDonationRequests(page: Int) {
        viewModelScope.launch {
            _donationRequestsState.value = RequestState.Loading
            _donationRequestsState.value = try {
                RequestState.Success(donationRequestRepo.getAll(page))
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to get data")
            }
        }
    }

    private fun createDonationRequest(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ) {
        _createDonationRequestState.value = RequestState.Loading

        viewModelScope.launch {
            _createDonationRequestState.value = try {
                val response = donationRequestRepo.insert(bloodGroup, beforeDate, contact, info)
                RequestState.Success(response)
            } catch (e: Exception) {
                RequestState.Error(e.message?: "Failed to create request")
            }
        }
    }
}