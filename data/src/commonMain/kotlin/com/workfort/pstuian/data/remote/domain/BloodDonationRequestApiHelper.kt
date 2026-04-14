package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.dto.BloodDonationRequestDto

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

abstract class BloodDonationRequestApiHelper : ApiHelper<BloodDonationRequestDto>()  {
    open suspend fun insert(
        userId: Int,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestDto = throw Exception("Not implemented yet")

    open suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): BloodDonationRequestDto = throw Exception("Not implemented yet")
}