package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.data.model.NetworkResult

abstract class BloodDonationRequestApiHelper : ApiHelper<BloodDonationRequestDto>() {

    open suspend fun insert(
        userId: String,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): NetworkResult<BloodDonationRequestDto> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): NetworkResult<BloodDonationRequestDto> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))
}