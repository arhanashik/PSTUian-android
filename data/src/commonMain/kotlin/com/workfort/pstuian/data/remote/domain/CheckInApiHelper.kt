package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.data.dto.CheckInDto

abstract class CheckInApiHelper : ApiHelper<CheckInDto>()  {
    open suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<CheckInDto> = emptyList()

    open suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<CheckInDto> = emptyList()

    open suspend fun getMyCheckIn(
        userId: Int,
        userType: String,
    ): CheckInDto? = null

    open suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String,
    ): CheckInDto? = null

    open suspend fun updatePrivacy(checkInId: Int, privacy: String, ): CheckInDto? = null
}