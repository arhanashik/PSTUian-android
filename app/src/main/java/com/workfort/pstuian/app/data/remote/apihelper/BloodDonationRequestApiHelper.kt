package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.blooddonationrequest.BloodDonationRequestEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 5:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/2/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

abstract class BloodDonationRequestApiHelper : ApiHelper<BloodDonationRequestEntity>()  {
    open suspend fun insert(
        userId: Int,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestEntity = throw Exception("Not implemented yet")

    open suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): BloodDonationRequestEntity = throw Exception("Not implemented yet")
}