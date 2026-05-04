package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

abstract class BloodDonationRequestApiHelper : ApiHelper<BloodDonationRequestDto>() {

    open suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<BloodDonationRequestDto>> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun insert(
        userId: Int,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): NetworkResult<Unit> = NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): NetworkResult<Unit> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))
}