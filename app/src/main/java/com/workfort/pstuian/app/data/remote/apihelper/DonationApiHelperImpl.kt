package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.donor.DonorEntity
import com.workfort.pstuian.util.remote.DonationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:47 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class DonationApiHelperImpl(private val service: DonationApiService) : DonationApiHelper{
    override suspend fun getDonationOption(): String {
        val response = service.getDonationOption()
        if(!response.success) throw Exception(response.message)

        return response.data?: ""
    }

    override suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String
    ): Int {
        val response = service.saveDonation(name, info, email, reference)
        if(!response.success) throw Exception(response.message)

        return response.data?: -1
    }

    override suspend fun getDonors(): List<DonorEntity> {
        val response = service.getDonors()
        if(!response.success) throw Exception(response.message)

        return response.data?: emptyList()
    }
}