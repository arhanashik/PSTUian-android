package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Donor

interface DonationRepository {
    suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String,
    ): DomainResult<Unit>

    suspend fun getDonors(page: Int, forceRefresh: Boolean = false): DomainResult<List<Donor>>
}