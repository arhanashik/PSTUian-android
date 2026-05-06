package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.DonorDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface DonationApiHelper {
    suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String,
    ): NetworkResult<Unit>

    suspend fun getDonors(
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<DonorDto>>
}