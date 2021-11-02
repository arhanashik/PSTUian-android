package com.workfort.pstuian.app.ui.donors.viewmodel

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.repository.DonationRepository
import com.workfort.pstuian.app.ui.donors.intent.DonorsIntent
import com.workfort.pstuian.app.ui.donors.viewstate.DonationOptionState
import com.workfort.pstuian.app.ui.donors.viewstate.DonationState
import com.workfort.pstuian.app.ui.donors.viewstate.DonorsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class DonorsViewModel(
    private val donationRepo: DonationRepository
) : ViewModel() {
    val intent = Channel<DonorsIntent>(Channel.UNLIMITED)

    private val _donationOptionState = MutableStateFlow<DonationOptionState>(DonationOptionState.Idle)
    val donationOptionState: StateFlow<DonationOptionState> get() = _donationOptionState

    private val _donorsState = MutableStateFlow<DonorsState>(DonorsState.Idle)
    val donorsState: StateFlow<DonorsState> get() = _donorsState

    private val _donationState = MutableStateFlow<DonationState>(DonationState.Idle)
    val donationState: StateFlow<DonationState> get() = _donationState

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
            _donorsState.value = DonorsState.Loading
            _donorsState.value = try {
                DonorsState.Donors(donationRepo.getDonors())
            } catch (e: Exception) {
                DonorsState.Error(e.message)
            }
        }
    }

    private fun getDonationOptions() {
        viewModelScope.launch {
            _donationOptionState.value = DonationOptionState.Loading
            _donationOptionState.value = try {
                val option = donationRepo.getDonationOption()
                Prefs.donateOption = option
                DonationOptionState.Success(option)
            } catch (e: Exception) {
                Timber.e(e)
                DonationOptionState.Error(e.message)
            }
        }
    }

    fun saveDonation(name: String, info: String, email: String, reference: String) {
        _donationState.value = DonationState.Loading

        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(info) || TextUtils.isEmpty(email)
            || TextUtils.isEmpty(reference)) {
            _donationState.value = DonationState.Error("All fields are required")
            return
        }

        viewModelScope.launch {
            Timber.e("store $name, $info, $email, $reference")
            _donationState.value = try {
                val response = donationRepo.saveDonation(name, info, email, reference)
                Prefs.donationId = response.toString()
                DonationState.Success("Donation is under review! Thanks for your help.")
            } catch (e: Exception) {
                Prefs.donationId = ""
                DonationState.Error(e.message)
            }
        }
    }
}