package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.DonorDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.data.remote.service.DonationApiService

class DonationApiHelperImpl(private val service: DonationApiService) : DonationApiHelper {

    override suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String
    ): NetworkResult<Unit> {
        return runCatching {
            service.saveDonation(name, info, email, reference).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }

    override suspend fun getDonors(): NetworkResult<List<DonorDto>> {
        return runCatching {
            service.getDonors().toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }
}