package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.data.model.toDto
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository

class BloodDonationRepositoryImpl(
    private val helper: BloodDonationApiHelper,
) : BloodDonationRepository {
    private val bloodDonationsCache = mutableMapOf<String, List<BloodDonationEntity>>()

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<BloodDonationEntity>> {
        if (forceRefresh) bloodDonationsCache.clear()

        val key = "$userId-$userType-$page"
        val cache = bloodDonationsCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getAll(userId, userType, page)
            .toDomainResult()
            .map { list -> list.map { it.toModel() } }
            .onSuccess { bloodDonationsCache[key] = it }
    }

    override suspend fun get(id: Int): DomainResult<BloodDonationEntity> {
        return helper.get(id).toDomainResult().map { it.toModel() }
    }

    override suspend fun insert(
        requestId: Int,
        userId: Int,
        userType: UserType,
        date: String,
        info: String,
    ): DomainResult<BloodDonationEntity> {
        return helper.insert(userId, userType.type, requestId, date, info)
            .toDomainResult()
            .map { it.toModel() }
    }

    override suspend fun update(
        id: Int,
        requestId: Int,
        date: String,
        info: String,
    ): DomainResult<Unit> {
        return helper.update(id, requestId, date, info).toDomainResult()
    }

    override suspend fun delete(id: Int): DomainResult<Unit> {
        return helper.delete(id).toDomainResult()
    }
}