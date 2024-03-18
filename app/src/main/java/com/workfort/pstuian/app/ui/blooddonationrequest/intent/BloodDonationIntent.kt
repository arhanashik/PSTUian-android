package com.workfort.pstuian.app.ui.blooddonationrequest.intent

import com.workfort.pstuian.model.BloodDonationEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 10 Dec, 2021 at 20:36.
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

sealed class BloodDonationIntent {
    data class GetAllDonations(
        val userId: Int,
        val userType: String,
        val page: Int,
    ) : BloodDonationIntent()
    data class CreateDonation(
        val requestId: Int?,
        val date: String,
        val info: String?
    ) : BloodDonationIntent()
    data class UpdateDonation(val item: BloodDonationEntity) : BloodDonationIntent()
    data class DeleteDonation(val id: Int) : BloodDonationIntent()

    data class GetAllDonationRequests(val page: Int) : BloodDonationIntent()
    data class CreateDonationRequest(
        val bloodGroup: String,
        val beforeDate: String,
        val contact: String,
        val info: String?,
    ) : BloodDonationIntent()
}