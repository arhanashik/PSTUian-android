package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CheckInLocationDto
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.data.remote.service.CheckInLocationApiService

class CheckInLocationApiHelperImpl(
    private val service: CheckInLocationApiService,
) : CheckInLocationApiHelper() {

    override suspend fun getAll(
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInLocationDto>> {
        return runCatching {
            service.getAll(page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun get(id: Int): NetworkResult<CheckInLocationDto> {
        return runCatching {
            service.get(id).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun search(
        query: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInLocationDto>> {
        return runCatching {
            service.search(query, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun insert(
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): NetworkResult<Unit> {
        return runCatching {
            service.insert(userType, name, details, imageUrl, link).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}