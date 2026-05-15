package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface BloodDonationRequestApiHelper {
    suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<BloodDonationRequestDto>>

    suspend fun get(id: Int): NetworkResult<BloodDonationRequestDto>

    suspend fun insert(
        userId: Int,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): NetworkResult<Unit>

    suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): NetworkResult<Unit>

    suspend fun markAsComplete(
        id: Int,
        userId: Int,
        userType: String,
    ): NetworkResult<Unit>

    suspend fun delete(id: Int): NetworkResult<Unit>
}
