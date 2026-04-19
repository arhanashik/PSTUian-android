package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.CheckInLocationDto

abstract class CheckInLocationApiHelper : ApiHelper<CheckInLocationDto>() {

    override suspend fun search(
        query: String,
        page: Int,
        limit: Int
    ): List<CheckInLocationDto> = emptyList()

    open suspend fun insert(
        userId: String,
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): CheckInLocationDto? = null
}