package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.CheckInLocationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.data.remote.service.CheckInLocationApiService

class CheckInLocationApiHelperImpl(
    private val service: CheckInLocationApiService
) : CheckInLocationApiHelper() {

    override suspend fun getAll(page: Int, limit: Int): NetworkResult<List<CheckInLocationDto>> {
        return service.getAll(page, limit).toNetworkResult()
    }

    override suspend fun get(id: Int): CheckInLocationDto {
        service.get(id).also {
            if(!it.isSuccess) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun search(
        query: String,
        page: Int,
        limit: Int
    ): List<CheckInLocationDto> {
        service.search(query, page, limit).also {
            if(!it.isSuccess) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun insert(
        userId: String,
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?
    ): CheckInLocationDto {
        service.insert(userId, userType, name, details, imageUrl, link).also {
            if(!it.isSuccess) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }
}