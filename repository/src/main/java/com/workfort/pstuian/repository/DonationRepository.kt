package com.workfort.pstuian.repository

import com.workfort.pstuian.networking.DonationApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:50 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class DonationRepository(private val helper: com.workfort.pstuian.networking.DonationApiHelper) {
    suspend fun getDonationOption() = helper.getDonationOption()
    suspend fun saveDonation(name: String, info: String, email: String, reference: String)
    = helper.saveDonation(name, info, email, reference)
    suspend fun getDonors() = helper.getDonors()
}