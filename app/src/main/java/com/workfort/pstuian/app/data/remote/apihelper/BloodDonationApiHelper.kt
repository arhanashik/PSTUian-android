package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.blooddonation.BloodDonationEntity
import com.workfort.pstuian.app.data.local.constant.Const

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

abstract class BloodDonationApiHelper : ApiHelper<BloodDonationEntity>()  {
    open suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = Const.Params.Default.PAGE_SIZE
    ): List<BloodDonationEntity> = emptyList()

    open suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: String,
        info: String?,
    ): BloodDonationEntity? = null
}