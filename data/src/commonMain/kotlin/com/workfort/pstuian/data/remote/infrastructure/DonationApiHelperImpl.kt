package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.DonorDto
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.data.remote.service.DonationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:47 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class DonationApiHelperImpl(private val service: DonationApiService) :
    DonationApiHelper {
    override suspend fun getDonationOption(): String {
        val response = service.getDonationOption()
        if(!response.isSuccess) throw Exception(response.message)

        return response.data?: ""
    }

    override suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String
    ): Int {
        val response = service.saveDonation(name, info, email, reference)
        if(!response.isSuccess) throw Exception(response.message)

        return response.data?: -1
    }

    override suspend fun getDonors(): List<DonorDto> {
        val response = service.getDonors()
        if(!response.isSuccess) throw Exception(response.message)

        return response.data?: emptyList()
    }
}