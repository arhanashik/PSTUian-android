package com.workfort.pstuian.app.ui.donors.viewstate

import com.workfort.pstuian.app.data.local.donor.DonorEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 7:52 AM.
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

sealed class DonationOptionState {
    object Idle : DonationOptionState()
    object Loading : DonationOptionState()
    data class Success(val option: String) : DonationOptionState()
    data class Error(val error: String?) : DonationOptionState()
}

sealed class DonorsState {
    object Idle : DonorsState()
    object Loading : DonorsState()
    data class Donors(val donors: List<DonorEntity>) : DonorsState()
    data class Error(val error: String?) : DonorsState()
}

sealed class DonationState {
    object Idle : DonationState()
    object Loading : DonationState()
    data class Success(val message: String) : DonationState()
    data class Error(val error: String?) : DonationState()
}