package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.CheckInLocationApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.CheckInLocationRepository


class CheckInLocationRepositoryImpl(
    private val authRepo: AuthRepository,
    private val helper: CheckInLocationApiHelper,
) : CheckInLocationRepository {
    override suspend fun getAll(page: Int) = helper.getAll(page).map { it.toEntity() }

    override suspend fun get(id: Int) = helper.get(id).toEntity()

    override suspend fun search(query: String, page: Int) =
        helper.search(query, page).map { it.toEntity() }

    override suspend fun insert(
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): CheckInLocationEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.insert(
            userIdAndType.first,
            userIdAndType.second,
            name,
            details,
            imageUrl,
            link,
        )?.toEntity() ?: throw Exception("Failed")
    }
}