package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.DonorDto
import com.workfort.pstuian.data.model.NetworkResult

interface DonationApiHelper {
    suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String,
    ): NetworkResult<Unit>

    suspend fun getDonors(): NetworkResult<List<DonorDto>>
}