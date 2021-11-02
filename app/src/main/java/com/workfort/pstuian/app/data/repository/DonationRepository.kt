package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.remote.apihelper.DonationApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:50 AM.
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

class DonationRepository(private val helper: DonationApiHelper) {
    suspend fun getDonationOption() = helper.getDonationOption()
    suspend fun saveDonation(name: String, info: String, email: String, reference: String)
    = helper.saveDonation(name, info, email, reference)
    suspend fun getDonors() = helper.getDonors()
}