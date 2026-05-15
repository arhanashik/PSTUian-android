package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.BloodDonationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.BloodDonationApiService

class BloodDonationApiHelperImpl(
    private val service: BloodDonationApiService,
) : BloodDonationApiHelper {

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<BloodDonationDto>> {
        return safeApiCall { service.getAll(userId, userType, page, limit) }
    }

    override suspend fun get(id: Int): NetworkResult<BloodDonationDto> {
        return safeApiCall { service.get(id) }
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int,
        date: String,
        info: String,
    ): NetworkResult<BloodDonationDto> {
        return safeApiCall { service.insert(userId, userType, requestId, date, info) }
    }

    override suspend fun update(
        id: Int,
        requestId: Int,
        date: String,
        info: String,
    ): NetworkResult<Unit> {
        return safeApiCall { service.update(id, requestId, date, info) }
    }

    override suspend fun delete(id: Int): NetworkResult<Unit> {
        return safeApiCall { service.delete(id) }
    }
}
