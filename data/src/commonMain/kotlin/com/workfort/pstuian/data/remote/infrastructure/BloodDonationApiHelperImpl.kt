package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.BloodDonationDto
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.data.remote.service.BloodDonationApiService

class BloodDonationApiHelperImpl(
    private val service: BloodDonationApiService,
) : BloodDonationApiHelper() {

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int
    ): NetworkResult<List<BloodDonationDto>> {
        return runCatching {
            service.getAll(userId, userType, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun get(id: Int): NetworkResult<BloodDonationDto> {
        return runCatching {
            service.get(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: Long,
        info: String?
    ): NetworkResult<BloodDonationDto> {
        return runCatching {
            service.insert(userId, userType, requestId, date, info).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun update(item: BloodDonationDto): NetworkResult<Unit> {
        return runCatching {
            service.update(item.id, item.requestId, item.date, item.info).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun delete(id: Int): NetworkResult<Unit> {
        return runCatching {
            service.delete(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}