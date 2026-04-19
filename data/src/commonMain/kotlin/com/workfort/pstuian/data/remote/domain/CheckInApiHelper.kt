package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.data.model.CheckInDto

abstract class CheckInApiHelper : ApiHelper<CheckInDto>()  {

    open suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<CheckInDto> = emptyList()

    open suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<CheckInDto> = emptyList()

    open suspend fun getMyCheckIn(
        userId: String,
        userType: String,
    ): CheckInDto? = null

    open suspend fun checkIn(
        locationId: Int,
        userId: String,
        userType: String,
    ): CheckInDto? = null

    open suspend fun updatePrivacy(checkInId: Int, privacy: String, ): CheckInDto? = null
}