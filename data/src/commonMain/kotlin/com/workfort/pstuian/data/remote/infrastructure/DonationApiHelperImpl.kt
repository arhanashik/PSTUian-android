package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.DonationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.data.remote.service.DonationApiService

class DonationApiHelperImpl(private val service: DonationApiService) : DonationApiHelper {

    override suspend fun saveDonation(
        name: String,
        email: String,
        reference: String,
        amount: String,
        message: String,
    ): NetworkResult<Unit> {
        return runCatching {
            service.saveDonation(name, email, reference, amount, message).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }

    override suspend fun getDonors(
        page: Int,
        limit: Int,
    ): NetworkResult<List<DonationDto>> {
        return runCatching {
            service.getDonors(page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }
}