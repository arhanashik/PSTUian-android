package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.CheckInLocationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.CheckInLocationApiService

class CheckInLocationApiHelperImpl(
    private val service: CheckInLocationApiService,
) : CheckInLocationApiHelper {

    override suspend fun getAll(
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInLocationDto>> {
        return safeApiCall { service.getAll(page, limit) }
    }

    override suspend fun get(id: Int): NetworkResult<CheckInLocationDto> {
        return safeApiCall { service.get(id) }
    }

    override suspend fun search(
        query: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CheckInLocationDto>> {
        return safeApiCall { service.search(query, page, limit) }
    }

    override suspend fun insert(
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): NetworkResult<Unit> {
        return safeApiCall { service.insert(userType, name, details, imageUrl, link) }
    }
}
