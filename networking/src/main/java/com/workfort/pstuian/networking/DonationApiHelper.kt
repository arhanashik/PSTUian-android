package com.workfort.pstuian.networking

import com.workfort.pstuian.model.DonorEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:01 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface DonationApiHelper {
    suspend fun getDonationOption(): String
    suspend fun saveDonation(name: String, info: String, email: String, reference: String): Int
    suspend fun getDonors(): List<DonorEntity>
}