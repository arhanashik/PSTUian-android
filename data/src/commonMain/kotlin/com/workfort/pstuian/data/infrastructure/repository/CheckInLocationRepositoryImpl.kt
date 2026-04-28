package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository

class CheckInLocationRepositoryImpl(
    private val helper: CheckInLocationApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : CheckInLocationRepository {

    private val cache = mutableListOf<CheckInLocation>()

    override suspend fun getAll(page: Int): List<CheckInLocation>{
        helper.getAll(page, limit = 20).toDomainResult(domainErrorMapper).map { dtos ->
            val data = dtos.map { it.toModel() }
            cache.clear()
            cache.addAll(data)
        }

        return cache
    }

    override suspend fun get(id: Int): DomainResult<CheckInLocation> {
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