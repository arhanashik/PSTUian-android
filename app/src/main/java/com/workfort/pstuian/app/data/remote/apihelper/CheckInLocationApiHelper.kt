package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.checkinlocation.CheckInLocationEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 21:30 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 12/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

abstract class CheckInLocationApiHelper : ApiHelper<CheckInLocationEntity>()  {
    open suspend fun search(
        query: String,
        page: Int,
        limit: Int = 10
    ): List<CheckInLocationEntity> = emptyList()

    open suspend fun insert(
        userId: Int,
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): CheckInLocationEntity? = null
}