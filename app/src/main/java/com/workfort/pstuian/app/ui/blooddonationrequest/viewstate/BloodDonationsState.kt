package com.workfort.pstuian.app.ui.blooddonationrequest.viewstate

import com.workfort.pstuian.app.data.local.blooddonation.BloodDonationEntity
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
    data class Donations(val data: List<BloodDonationEntity>) : BloodDonationsState()
    data class Error(val message: String) : BloodDonationsState()
}

sealed class BloodDonationState {
    object Idle: BloodDonationState()
    object Loading: BloodDonationState()
    data class Success(val item: BloodDonationEntity) : BloodDonationState()
    data class Error(val message: String) : BloodDonationState()
}

sealed class ItemDeleteState {
    object Idle: ItemDeleteState()
    object Loading: ItemDeleteState()
    data class Success(val itemId: Int) : ItemDeleteState()
    data class Error(val message: String) : ItemDeleteState()
}

sealed class BloodDonationRequestsState {
    object Idle: BloodDonationRequestsState()
    object Loading: BloodDonationRequestsState()
    data class DonationRequests(val data: List<BloodDonationRequestEntity>) : BloodDonationRequestsState()
    data class Error(val message: String) : BloodDonationRequestsState()
}

sealed class BloodDonationRequestState {
    object Idle: BloodDonationRequestState()
    object Loading: BloodDonationRequestState()
    data class Success(val data: BloodDonationRequestEntity) : BloodDonationRequestState()
    data class Error(val message: String) : BloodDonationRequestState()
}