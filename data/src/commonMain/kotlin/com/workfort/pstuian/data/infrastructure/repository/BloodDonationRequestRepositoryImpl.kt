package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationRequest
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository

class BloodDonationRequestRepositoryImpl(
    private val helper: BloodDonationRequestApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : BloodDonationRequestRepository {

    private val bloodDonationRequestCache = mutableMapOf<Int, List<BloodDonationRequest>>()

    override suspend fun getAll(
        userId: Int,
        userType: UserType,
        page: Int,
        forceRefresh: Boolean,
    ) : DomainResult<List<BloodDonationRequest>> {
        if (forceRefresh) bloodDonationRequestCache.clear()

        val cache = bloodDonationRequestCache[page]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getAll(userId = userId, userType = userType.type, page = page)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { bloodDonationRequestCache[page] = it }
    }

    override suspend fun get(id: Int) = when (val result = helper.get(id)) {
        is NetworkResult.Success -> result.value.toModel()
        is NetworkResult.Failure -> throw result.error
    }

    override suspend fun insert(
        userId: Int,
        userType: UserType,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): DomainResult<Unit> {
        return helper.insert(
            userId,
            userType.type,
            bloodGroup,
            beforeDate,
            contact,
            info,
        ).toDomainResult(domainErrorMapper)
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): DomainResult<Unit> {
        return helper.update(id, bloodGroup, beforeDate, contact, info).toDomainResult(domainErrorMapper)
    }

    override suspend fun delete(id: Int): DomainResult<Unit> {
        return helper.delete(id).toDomainResult(domainErrorMapper)
    }
}