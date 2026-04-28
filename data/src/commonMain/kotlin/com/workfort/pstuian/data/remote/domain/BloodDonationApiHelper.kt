package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationDto
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

abstract class BloodDonationApiHelper : ApiHelper<BloodDonationDto>()  {

    open suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): NetworkResult<List<BloodDonationDto>> = super.getAll(page, limit)

    open suspend fun insert(
        userId: String,
        userType: String,
        requestId: Int?,
        date: Long,
        info: String?,
    ): NetworkResult<BloodDonationDto> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))
}