package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.BloodDonationRequestApiService

class BloodDonationRequestApiHelperImpl(
    private val service: BloodDonationRequestApiService,
) : BloodDonationRequestApiHelper {

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<BloodDonationRequestDto>> {
        return safeApiCall { service.getAll(userId, userType, page, limit) }
    }

    override suspend fun get(id: Int): NetworkResult<BloodDonationRequestDto> {
        return safeApiCall { service.get(id) }
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): NetworkResult<Unit> {
        return safeApiCall {
            service.insert(userId, userType, bloodGroup, beforeDate, contact, info)
        }
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): NetworkResult<Unit> {
        return safeApiCall { service.update(id, bloodGroup, beforeDate, contact, info) }
    }

    override suspend fun markAsComplete(id: Int, userId: Int, userType: String): NetworkResult<Unit> {
        return safeApiCall { service.markAsComplete(id, userId, userType) }
    }

    override suspend fun delete(id: Int): NetworkResult<Unit> {
        return safeApiCall { service.delete(id) }
    }
}
