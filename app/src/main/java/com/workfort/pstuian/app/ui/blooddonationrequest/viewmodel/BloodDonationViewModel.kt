package com.workfort.pstuian.app.ui.blooddonationrequest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.blooddonation.BloodDonationEntity
import com.workfort.pstuian.app.data.repository.BloodDonationRepository
import com.workfort.pstuian.app.data.repository.BloodDonationRequestRepository
import com.workfort.pstuian.app.ui.blooddonationrequest.intent.BloodDonationIntent
import com.workfort.pstuian.app.ui.blooddonationrequest.viewstate.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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
 *  *
 *  * Last edited by : arhan on 12/10/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class BloodDonationViewModel(
    private val donationRepo: BloodDonationRepository,
    private val donationRequestRepo: BloodDonationRequestRepository,
) : ViewModel() {
    val intent = Channel<BloodDonationIntent>(Channel.UNLIMITED)

    // blood donation states
    var donationsPage = 1
    private val _donationsState = MutableStateFlow<BloodDonationsState>(BloodDonationsState.Idle)
    val donationsState: StateFlow<BloodDonationsState> get() = _donationsState

    private val _createDonationState = MutableStateFlow<BloodDonationState>(BloodDonationState.Idle)
    val createDonationState: StateFlow<BloodDonationState> get() = _createDonationState

    private val _updateDonationState = MutableStateFlow<BloodDonationState>(BloodDonationState.Idle)
    val updateDonationState: StateFlow<BloodDonationState> get() = _updateDonationState

    private val _deleteDonationState = MutableStateFlow<ItemDeleteState>(ItemDeleteState.Idle)
    val deleteDonationState: StateFlow<ItemDeleteState> get() = _deleteDonationState

    // blood donation request states
    var donationRequestsPage = 1
    private val _donationRequestsState = MutableStateFlow<BloodDonationRequestsState>(BloodDonationRequestsState.Idle)
    val donationRequestsState: StateFlow<BloodDonationRequestsState> get() = _donationRequestsState

    private val _createDonationRequestState = MutableStateFlow<BloodDonationRequestState>(BloodDonationRequestState.Idle)
    val createDonationRequestState: StateFlow<BloodDonationRequestState> get() = _createDonationRequestState

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
            _donationsState.value = BloodDonationsState.Loading
            _donationsState.value = try {
                BloodDonationsState.Donations(donationRepo.getAll(userId, userType, page))
            } catch (e: Exception) {
                BloodDonationsState.Error(e.message?: "Failed to get data")
            }
        }
    }

    private fun createDonation(requestId: Int?, date: String, info: String?) {
        _createDonationState.value = BloodDonationState.Loading

        viewModelScope.launch {
            _createDonationState.value = try {
                val response = donationRepo.insert(requestId, date, info)
                BloodDonationState.Success(response)
            } catch (e: Exception) {
                BloodDonationState.Error(e.message?: "Failed to create donation")
            }
        }
    }

    private fun updateDonation(item: BloodDonationEntity) {
        viewModelScope.launch {
            _updateDonationState.value = BloodDonationState.Loading
            _updateDonationState.value = try {
                BloodDonationState.Success(donationRepo.update(item))
            } catch (e: Exception) {
                BloodDonationState.Error(e.message?: "Failed to update")
            }
        }
    }

    private fun deleteDonation(id: Int) {
        viewModelScope.launch {
            _deleteDonationState.value = ItemDeleteState.Loading
            _deleteDonationState.value = try {
                if(donationRepo.delete(id)) {
                    ItemDeleteState.Success(id)
                } else {
                    ItemDeleteState.Error("Failed to delete")
                }
            } catch (e: Exception) {
                ItemDeleteState.Error(e.message?: "Failed to delete")
            }
        }
    }

    private fun getDonationRequests(page: Int) {
        viewModelScope.launch {
            _donationRequestsState.value = BloodDonationRequestsState.Loading
            _donationRequestsState.value = try {
                BloodDonationRequestsState.DonationRequests(donationRequestRepo.getAll(page))
            } catch (e: Exception) {
                BloodDonationRequestsState.Error(e.message?: "Failed to get data")
            }
        }
    }

    private fun createDonationRequest(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ) {
        _createDonationRequestState.value = BloodDonationRequestState.Loading

        viewModelScope.launch {
            _createDonationRequestState.value = try {
                val response = donationRequestRepo.insert(bloodGroup, beforeDate, contact, info)
                BloodDonationRequestState.Success(response)
            } catch (e: Exception) {
                BloodDonationRequestState.Error(e.message?: "Failed to create request")
            }
        }
    }
}