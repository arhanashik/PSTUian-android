package com.workfort.pstuian.app.ui.blooddonation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.repository.BloodDonationRepository
import com.workfort.pstuian.app.data.repository.BloodDonationRequestRepository
import com.workfort.pstuian.app.ui.blooddonation.intent.BloodDonationIntent
import com.workfort.pstuian.app.ui.blooddonation.viewstate.BloodDonationRequestsState
import com.workfort.pstuian.app.ui.blooddonation.viewstate.BloodDonationsState
import com.workfort.pstuian.app.ui.blooddonation.viewstate.CreateBloodDonationRequestsState
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

    var donationPage = 1
    private val _donationsState = MutableStateFlow<BloodDonationsState>(BloodDonationsState.Idle)
    val donationsState: StateFlow<BloodDonationsState> get() = _donationsState

    var donationRequestPage = 1
    private val _donationRequestsState = MutableStateFlow<BloodDonationRequestsState>(BloodDonationRequestsState.Idle)
    val donationRequestsState: StateFlow<BloodDonationRequestsState> get() = _donationRequestsState

    private val _createDonationRequestState = MutableStateFlow<CreateBloodDonationRequestsState>(CreateBloodDonationRequestsState.Idle)
    val createDonationRequestState: StateFlow<CreateBloodDonationRequestsState> get() = _createDonationRequestState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is BloodDonationIntent.GetAllDonations -> getDonations(donationPage)
                    is BloodDonationIntent.GetAllDonationRequests -> getDonationRequests(donationRequestPage)
                }
            }
        }
    }

    private fun getDonations(page: Int) {

    }

    private fun getDonationRequests(page: Int) {
        viewModelScope.launch {
            _donationRequestsState.value = BloodDonationRequestsState.Loading
            _donationRequestsState.value = try {
                BloodDonationRequestsState.DonationRequests(donationRequestRepo.getAll(page))
            } catch (e: Exception) {
                BloodDonationRequestsState.Error(e.message)
            }
        }
    }

    fun createDonationRequest(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) {
        _createDonationRequestState.value = CreateBloodDonationRequestsState.Loading

        viewModelScope.launch {
            _createDonationRequestState.value = try {
                val response = donationRequestRepo.insert(bloodGroup, beforeDate, contact, info)
                CreateBloodDonationRequestsState.Success(response)
            } catch (e: Exception) {
                CreateBloodDonationRequestsState.Error(e.message)
            }
        }
    }
}