package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.CheckInLocationDto
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.data.model.NetworkResult

abstract class CheckInLocationApiHelper : ApiHelper<CheckInLocationDto>() {

    open suspend fun insert(
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): NetworkResult<Unit> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))
}