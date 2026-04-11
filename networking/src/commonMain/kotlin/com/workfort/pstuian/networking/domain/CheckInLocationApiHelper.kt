package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.model.dto.CheckInLocationDto

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 21:30 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

abstract class CheckInLocationApiHelper : ApiHelper<CheckInLocationDto>()  {
    override suspend fun search(
        query: String,
        page: Int,
        limit: Int
    ): List<CheckInLocationDto> = emptyList()

    open suspend fun insert(
        userId: Int,
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): CheckInLocationDto? = null
}