package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.data.remote.service.BloodDonationRequestApiService

class BloodDonationRequestApiHelperImpl(
    private val service: BloodDonationRequestApiService
) : BloodDonationRequestApiHelper() {

    override suspend fun getAll(page: Int, limit: Int): NetworkResult<List<BloodDonationRequestDto>> {
        return service.getAll(page, limit).toNetworkResult()
    }

    override suspend fun get(id: Int): BloodDonationRequestDto {
        val response = service.get(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun insert(
        userId: String,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?
    ): BloodDonationRequestDto {
        val response = service.insert(userId, userType, bloodGroup, beforeDate, contact, info)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String
    ): BloodDonationRequestDto {
        val response = service.update(id, bloodGroup, beforeDate, contact, info)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun delete(id: Int): Boolean {
        val response = service.delete(id)
        if(!response.success) throw Exception(response.message)

        return true
    }
}