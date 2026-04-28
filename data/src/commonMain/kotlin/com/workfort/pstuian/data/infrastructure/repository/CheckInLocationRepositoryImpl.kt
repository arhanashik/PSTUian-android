package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository

class CheckInLocationRepositoryImpl(
    private val helper: CheckInLocationApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : CheckInLocationRepository {

    private val checkInLocationsCache = mutableMapOf<Int, List<CheckInLocation>>()

    override suspend fun getAll(page: Int, forceRefresh: Boolean) : DomainResult<List<CheckInLocation>> {
        val cache = checkInLocationsCache[page]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getAll(page)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { checkInLocationsCache[page] = it }
    }

    override suspend fun get(id: Int): DomainResult<CheckInLocation> {
        checkInLocationsCache.values.flatten().firstOrNull { it.id == id }?.let {
            return DomainResult.success(it)
        }

        return helper.get(id).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun search(query: String, page: Int) =
        when (val result = helper.search(query, page)) {
            is NetworkResult.Success -> result.value.map { it.toModel() }
            is NetworkResult.Failure -> throw result.error
        }

    override suspend fun insert(
        userId: String,
        userType: UserType,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): CheckInLocation {
        return when (val result = helper.insert(
            userId,
            userType.type,
            name,
            details,
            imageUrl,
            link,
        )) {
            is NetworkResult.Success -> result.value.toModel()
            is NetworkResult.Failure -> throw result.error
        }
    }
}