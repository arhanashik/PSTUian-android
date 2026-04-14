package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.featuredomain.repository.DonationRepository

class DonationRepositoryImpl(private val helper: DonationApiHelper) : DonationRepository {
    override suspend fun getDonationOption() = helper.getDonationOption()

    override suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String,
    ) = helper.saveDonation(name, info, email, reference)

    override suspend fun getDonors() = helper.getDonors().map { it.toEntity() }
}