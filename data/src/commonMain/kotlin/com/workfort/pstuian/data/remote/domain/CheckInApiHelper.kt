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

    suspend fun getHistory(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CheckInDto>>

    suspend fun getCheckIn(userId: Int, userType: String, ): NetworkResult<CheckInDto>

    suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String,
    ): NetworkResult<Unit>

    suspend fun updatePrivacy(checkInId: Int, privacy: String, ): NetworkResult<Unit>

    suspend fun delete(id: Int): NetworkResult<Unit>
}