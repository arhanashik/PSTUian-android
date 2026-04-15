package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.NetworkConst
import com.workfort.pstuian.data.dto.BloodDonationDto

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

abstract class BloodDonationApiHelper : ApiHelper<BloodDonationDto>()  {
    open suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<BloodDonationDto> = emptyList()

    open suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: Long,
        info: String?,
    ): BloodDonationDto? = null
}