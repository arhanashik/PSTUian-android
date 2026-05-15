package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository

class CheckInLocationRepositoryImpl(
    private val helper: CheckInLocationApiHelper,
) : CheckInLocationRepository {

    private val checkInLocationsCache = mutableMapOf<Int, List<CheckInLocation>>()

    override suspend fun getAll(page: Int, forceRefresh: Boolean) : DomainResult<List<CheckInLocation>> {
        val cache = checkInLocationsCache[page]
        if (!forceRefresh && !cache.isNullOrEmpty()) {
            return DomainResult.success(cache)
        }

        return helper.getAll(page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { checkInLocationsCache[page] = it }
    }

    override suspend fun get(id: Int): DomainResult<CheckInLocation> {
        checkInLocationsCache.values.flatten().firstOrNull { it.id == id }?.let {
            return DomainResult.success(it)
        }

        return helper.get(id).toDomainResult().map { it.toModel() }
    }

    override suspend fun search(query: String, page: Int): DomainResult<List<CheckInLocation>>{
        return helper.search(query, page).toDomainResult().map { list ->
            list.map { it.toModel() }
        }
    }

    override suspend fun insert(
        userType: UserType,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): DomainResult<Unit> {
        return helper.insert(
            userType.type,
            name,
            details,
            imageUrl,
            link,
        ).toDomainResult()
    }
}