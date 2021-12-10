package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.blooddonation.BloodDonation

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

abstract class BloodDonationApiHelper : ApiHelper<BloodDonation>()  {
    open suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: String,
        info: String,
    ): BloodDonation? = null

    open suspend fun update(
        id: Int,
        requestId: Int?,
        date: String,
        info: String,
    ): BloodDonation? = null
}