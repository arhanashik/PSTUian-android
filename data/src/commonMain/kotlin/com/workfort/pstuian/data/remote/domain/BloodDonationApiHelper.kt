package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationDto
import com.workfort.pstuian.data.remote.NetworkConst

abstract class BloodDonationApiHelper : ApiHelper<BloodDonationDto>()  {

    open suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<BloodDonationDto> = emptyList()

    open suspend fun insert(
        userId: String,
        userType: String,
        requestId: Int?,
        date: Long,
        info: String?,
    ): BloodDonationDto? = null
}