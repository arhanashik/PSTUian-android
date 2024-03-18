package com.workfort.pstuian.networking

import com.workfort.pstuian.model.BloodDonationRequestEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 5:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

abstract class BloodDonationRequestApiHelper : com.workfort.pstuian.networking.ApiHelper<BloodDonationRequestEntity>()  {
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