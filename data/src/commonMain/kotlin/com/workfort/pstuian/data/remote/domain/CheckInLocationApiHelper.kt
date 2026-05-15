package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.CheckInLocationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface CheckInLocationApiHelper {
    suspend fun getAll(
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CheckInLocationDto>>

    suspend fun get(id: Int): NetworkResult<CheckInLocationDto>

    suspend fun search(
        query: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<CheckInLocationDto>>

    suspend fun insert(
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): NetworkResult<Unit>
}
