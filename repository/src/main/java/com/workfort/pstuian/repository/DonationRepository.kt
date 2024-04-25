package com.workfort.pstuian.repository

import com.workfort.pstuian.networking.domain.DonationApiHelper


class DonationRepository(private val helper: DonationApiHelper) {
    suspend fun getDonationOption() = helper.getDonationOption()
    suspend fun saveDonation(name: String, info: String, email: String, reference: String) =
        helper.saveDonation(name, info, email, reference)

    suspend fun getDonors() = helper.getDonors()
}