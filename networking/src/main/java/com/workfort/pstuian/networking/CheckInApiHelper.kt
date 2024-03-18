package com.workfort.pstuian.networking

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.CheckInEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 12:16 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 12/13/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

abstract class CheckInApiHelper : com.workfort.pstuian.networking.ApiHelper<CheckInEntity>()  {
    open suspend fun getAll(
        locationId: Int,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<CheckInEntity> = emptyList()

    open suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
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