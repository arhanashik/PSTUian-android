package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository

class BloodDonationRequestRepositoryImpl(
    private val helper: BloodDonationRequestApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : BloodDonationRequestRepository {

    private val cache = mutableListOf<BloodDonationRequestEntity>()

    override suspend fun getAll(page: Int): List<BloodDonationRequestEntity> {
        helper.getAll(page, limit = 20).toDomainResult(domainErrorMapper).map { dtos ->
            val data = dtos.map { it.toEntity() }
            cache.clear()
            cache.addAll(data)
        }

        return cache
    }

    override suspend fun get(id: Int) = helper.get(id).toEntity()

    override suspend fun insert(
        userId: String,
        userType: UserType,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestEntity {
        return helper.insert(
            userId,
            userType.type,
            bloodGroup,
            beforeDate,
            contact,
            info
        ).toEntity()
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) = helper.update(id, bloodGroup, beforeDate, contact, info).toEntity()

    override suspend fun delete(id: Int) = helper.delete(id)
}