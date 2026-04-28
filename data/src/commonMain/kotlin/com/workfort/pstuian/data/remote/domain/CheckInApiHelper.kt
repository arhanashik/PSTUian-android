package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.CheckInDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface CheckInApiHelper  {

    suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CheckInDto>>

    suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CheckInDto>>

    suspend fun getMyCheckIn(userId: String, userType: String, ): NetworkResult<CheckInDto>

    suspend fun checkIn(
        locationId: Int,
        userId: String,
        userType: String,
    ): NetworkResult<CheckInDto>

    suspend fun updatePrivacy(checkInId: Int, privacy: String, ): NetworkResult<CheckInDto>

    suspend fun delete(id: Int): NetworkResult<Unit>
}