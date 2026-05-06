package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.DonationApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.Donor
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.DonationRepository

class DonationRepositoryImpl(
    private val helper: DonationApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : DonationRepository {

    override suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String,
    ): DomainResult<Unit> {
        return helper.saveDonation(name, info, email, reference).toDomainResult(domainErrorMapper)
    }

    override suspend fun getDonors(): DomainResult<List<Donor>> {
        return helper.getDonors()
            .toDomainResult(domainErrorMapper)
            .map { list -> list.map { it.toModel() } }
    }
}