package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository

class CheckInLocationRepositoryImpl(
    private val helper: CheckInLocationApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : CheckInLocationRepository {

    private val cache = mutableListOf<CheckInLocationEntity>()

    override suspend fun getAll(page: Int): List<CheckInLocationEntity>{
        helper.getAll(page, limit = 20).toDomainResult(domainErrorMapper).map { dtos ->
            val data = dtos.map { it.toEntity() }
            cache.clear()
            cache.addAll(data)
        }

        return cache
    }

    override suspend fun get(id: Int) = helper.get(id).toEntity()

    override suspend fun search(query: String, page: Int) =
        helper.search(query, page).map { it.toEntity() }

    override suspend fun insert(
        userId: String,
        userType: UserType,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): CheckInLocationEntity {
        return helper.insert(
            userId,
            userType.type,
            name,
            details,
            imageUrl,
            link,
        )?.toEntity() ?: throw Exception("Failed")
    }
}