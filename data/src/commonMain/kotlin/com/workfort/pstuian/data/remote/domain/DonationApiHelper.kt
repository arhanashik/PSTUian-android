package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.DonationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface DonationApiHelper {
    suspend fun saveDonation(
        name: String,
        email: String,
        reference: String,
        amount: String,
        message: String,
    ): NetworkResult<Unit>

    suspend fun getDonors(
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<DonationDto>>
}