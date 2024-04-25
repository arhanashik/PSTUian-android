package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.CheckInEntity

abstract class CheckInApiHelper : ApiHelper<CheckInEntity>()  {
    open suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<CheckInEntity> = emptyList()

    open suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<CheckInEntity> = emptyList()

    open suspend fun getMyCheckIn(
        userId: Int,
        userType: String,
    ): CheckInEntity? = null

    open suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String,
    ): CheckInEntity? = null

    open suspend fun updatePrivacy(checkInId: Int, privacy: String, ): CheckInEntity? = null
}