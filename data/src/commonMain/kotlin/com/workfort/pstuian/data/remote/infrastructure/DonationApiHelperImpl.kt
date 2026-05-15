package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.DonationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.DonationApiService

class DonationApiHelperImpl(private val service: DonationApiService) : DonationApiHelper {

    override suspend fun saveDonation(
        name: String,
        email: String,
        reference: String,
        amount: String,
        message: String,
    ): NetworkResult<Unit> {
        return safeApiCall { service.saveDonation(name, email, reference, amount, message) }
    }

    override suspend fun getDonors(
        page: Int,
        limit: Int,
    ): NetworkResult<List<DonationDto>> {
        return safeApiCall { service.getDonors(page, limit) }
    }
}
