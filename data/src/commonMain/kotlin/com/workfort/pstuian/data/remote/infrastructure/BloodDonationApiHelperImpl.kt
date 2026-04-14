package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.dto.BloodDonationDto
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.data.remote.service.BloodDonationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:47 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class BloodDonationApiHelperImpl(
    private val service: BloodDonationApiService
) : BloodDonationApiHelper() {
    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int
    ): List<BloodDonationDto> {
        service.getAll(userId, userType, page, limit).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun get(id: Int): BloodDonationDto {
        val response = service.get(id)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: Long,
        info: String?
    ): BloodDonationDto {
        val response = service.insert(userId, userType, requestId, date, info)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun update(item: BloodDonationDto): BloodDonationDto {
        val response = service.update(item.id, item.requestId, item.date, item.info)
        if(!response.success) throw Exception(response.message)

        return response.data?: throw Exception("No data")
    }

    override suspend fun delete(id: Int): Boolean {
        val response = service.delete(id)
        if(!response.success) throw Exception(response.message)

        return true
    }
}