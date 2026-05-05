package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.toDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository

class BloodDonationRepositoryImpl(
    private val helper: BloodDonationApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : BloodDonationRepository {

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
    ): DomainResult<List<BloodDonationEntity>> {
        return helper.getAll(userId, userType, page)
            .toDomainResult(domainErrorMapper)
            .map { list -> list.map { it.toModel() } }
    }

    override suspend fun get(id: Int): DomainResult<BloodDonationEntity> {
        return helper.get(id).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun insert(
        requestId: Int?,
        userId: Int,
        userType: UserType,
        date: Long,
        info: String?,
    ): DomainResult<BloodDonationEntity> {
        return helper.insert(userId, userType.type, requestId, date, info)
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
    }

    override suspend fun update(item: BloodDonationEntity): DomainResult<Unit> {
        return helper.update(item.toDto()).toDomainResult(domainErrorMapper)
    }

    override suspend fun delete(id: Int): DomainResult<Unit> {
        return helper.delete(id).toDomainResult(domainErrorMapper)
    }
}