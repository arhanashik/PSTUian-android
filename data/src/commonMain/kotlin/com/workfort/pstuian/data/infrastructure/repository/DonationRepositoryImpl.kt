package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Donation
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.DonationRepository

class DonationRepositoryImpl(
    private val helper: DonationApiHelper,
) : DonationRepository {

    private val donorsCache = mutableMapOf<Int, List<Donation>>()

    override suspend fun saveDonation(
        name: String,
        email: String,
        reference: String,
        amount: String,
        message: String,
    ): DomainResult<Unit> {
        return helper.saveDonation(name, email, reference, amount, message).toDomainResult()
    }

    override suspend fun getDonors(page: Int, forceRefresh: Boolean): DomainResult<List<Donation>> {
        if (forceRefresh) donorsCache.clear()

        val cache = donorsCache[page]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getDonors(page)
            .toDomainResult()
            .map { list -> list.map { it.toModel() } }
            .onSuccess { donorsCache[page] = it }
    }
}