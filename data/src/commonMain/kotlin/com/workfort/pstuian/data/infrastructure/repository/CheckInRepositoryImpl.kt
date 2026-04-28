package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.CheckInRepository

class CheckInRepositoryImpl(
    private val helper: CheckInApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : CheckInRepository {

    override suspend fun getAll(locationId: Int, page: Int): DomainResult<List<CheckIn>> {
        return helper.getAll(locationId = locationId, page = page)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
    }

    override suspend fun getAll(userId: String, userType: UserType, page: Int): DomainResult<List<CheckIn>> {
        return helper.getAll(userId = userId, userType = userType.type, page = page)
            .toDomainResult(domainErrorMapper)
            .map { dtos -> dtos.map { it.toModel() } }
    }

    override suspend fun getMyCheckIn(userId: String, userType: UserType): DomainResult<CheckIn> {
        return helper.getMyCheckIn(userId, userType.type).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun checkIn(
        userId: String,
        userType: UserType,
        locationId: Int,
    ): DomainResult<CheckIn> {
        return helper.checkIn(
            locationId,
            userId,
            userType.type,
        ).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String): DomainResult<CheckIn> {
        return helper.updatePrivacy(checkInId, privacy).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun delete(checkInId: Int): DomainResult<Unit> {
        return helper.delete(checkInId).toDomainResult(domainErrorMapper)
    }
}