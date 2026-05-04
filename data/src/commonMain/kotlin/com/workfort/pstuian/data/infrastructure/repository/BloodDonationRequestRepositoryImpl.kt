package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository

class BloodDonationRequestRepositoryImpl(
    private val helper: BloodDonationRequestApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : BloodDonationRequestRepository {

    private val bloodDonationRequestCache = mutableMapOf<Int, List<BloodDonationRequestEntity>>()

    override suspend fun getAll(page: Int, forceRefresh: Boolean): DomainResult<List<BloodDonationRequestEntity>> {
        if (forceRefresh) bloodDonationRequestCache.clear()

        val cache = bloodDonationRequestCache[page]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getAll(page)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { bloodDonationRequestCache[page] = it }
    }

    override suspend fun get(id: Int) = when (val result = helper.get(id)) {
        is NetworkResult.Success -> result.value.toModel()
        is NetworkResult.Failure -> throw result.error
    }

    override suspend fun insert(
        userId: String,
        userType: UserType,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestEntity {
        return when (val result = helper.insert(
            userId,
            userType.type,
            bloodGroup,
            beforeDate,
            contact,
            info
        )) {
            is NetworkResult.Success -> result.value.toModel()
            is NetworkResult.Failure -> throw result.error
        }
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) = when (val result = helper.update(id, bloodGroup, beforeDate, contact, info)) {
        is NetworkResult.Success -> result.value.toModel()
        is NetworkResult.Failure -> throw result.error
    }

    override suspend fun delete(id: Int) = when (helper.delete(id)) {
        is NetworkResult.Success -> true
        is NetworkResult.Failure -> false
    }
}