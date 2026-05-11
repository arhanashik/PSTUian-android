package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Donation

interface DonationRepository {
    suspend fun saveDonation(
        name: String,
        email: String,
        reference: String,
        amount: String,
        message: String,
    ): DomainResult<Unit>

    suspend fun getDonors(page: Int, forceRefresh: Boolean = false): DomainResult<List<Donation>>
}