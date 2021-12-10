package com.workfort.pstuian.app.ui.blooddonation.viewstate

import com.workfort.pstuian.app.data.local.blooddonation.BloodDonation
import com.workfort.pstuian.app.data.local.blooddonationrequest.BloodDonationRequestEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 20:32.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/10.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

sealed class BloodDonationsState {
    object Idle: BloodDonationsState()
    object Loading: BloodDonationsState()
    data class Donations(val data: List<BloodDonation>) : BloodDonationsState()
    data class Error(val error: String?) : BloodDonationsState()
}

sealed class BloodDonationRequestsState {
    object Idle: BloodDonationRequestsState()
    object Loading: BloodDonationRequestsState()
    data class DonationRequests(val data: List<BloodDonationRequestEntity>) : BloodDonationRequestsState()
    data class Error(val error: String?) : BloodDonationRequestsState()
}

sealed class CreateBloodDonationRequestsState {
    object Idle: CreateBloodDonationRequestsState()
    object Loading: CreateBloodDonationRequestsState()
    data class Success(val data: BloodDonationRequestEntity) : CreateBloodDonationRequestsState()
    data class Error(val error: String?) : CreateBloodDonationRequestsState()
}
