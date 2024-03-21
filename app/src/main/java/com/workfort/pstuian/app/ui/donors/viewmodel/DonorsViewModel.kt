package com.workfort.pstuian.app.ui.donors.viewmodel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.ui.donors.intent.DonorsIntent
import com.workfort.pstuian.model.RequestState
import com.workfort.pstuian.repository.DonationRepository
import com.workfort.pstuian.sharedpref.Prefs
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 7:56 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class DonorsViewModel(private val donationRepo: DonationRepository) : ViewModel() {
    val intent = Channel<DonorsIntent>(Channel.UNLIMITED)

    private val _donationOptionState = MutableStateFlow<RequestState>(RequestState.Idle)
    val donationOptionState: StateFlow<RequestState> get() = _donationOptionState

    private val _donorsState = MutableStateFlow<RequestState>(RequestState.Idle)
    val donorsState: StateFlow<RequestState> get() = _donorsState

    private val _donationState = MutableStateFlow<RequestState>(RequestState.Idle)
    val donationState: StateFlow<RequestState> get() = _donationState

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is DonorsIntent.GetDonors -> getDonors()
                    is DonorsIntent.GetDonationOptions -> getDonationOptions()
                }
            }
        }
    }

    private fun getDonors() {
        viewModelScope.launch {
            _donorsState.value = RequestState.Loading
            _donorsState.value = try {
                RequestState.Success(donationRepo.getDonors())
            } catch (e: Exception) {
                RequestState.Error(e.message)
            }
        }
    }

    private fun getDonationOptions() {
        viewModelScope.launch {
            _donationOptionState.value = RequestState.Loading
            _donationOptionState.value = try {
                val option = donationRepo.getDonationOption()
                Prefs.donateOption = option
                RequestState.Success(option)
            } catch (e: Exception) {
                Timber.e(e)
                RequestState.Error(e.message)
            }
        }
    }

    fun saveDonation(name: String, info: String, email: String, reference: String) {
        _donationState.value = RequestState.Loading

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(info) || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(reference)) {
            _donationState.value = RequestState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            Timber.e("store $name, $info, $email, $reference")
            _donationState.value = try {
                val response = donationRepo.saveDonation(name, info, email, reference)
                Prefs.donationId = response.toString()
                RequestState.Success("Donation is under review! Thanks for your help.")
            } catch (e: Exception) {
                Prefs.donationId = ""
                RequestState.Error(e.message)
            }
        }
    }
}