package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface BloodDonationApiHelper {
    suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<BloodDonationDto>>

    suspend fun get(id: Int): NetworkResult<BloodDonationDto>

    suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int,
        date: String,
        info: String,
    ): NetworkResult<BloodDonationDto>

    suspend fun update(
        id: Int,
        requestId: Int,
        date: String,
        info: String,
    ): NetworkResult<Unit>

    suspend fun delete(id: Int): NetworkResult<Unit>
}
