package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.CheckInRepository

class CheckInRepositoryImpl(
    private val helper: CheckInApiHelper,
) : CheckInRepository {

    private val checkInsCache = mutableMapOf<String, List<CheckIn>>()
    private val checkInsForUserCache = mutableMapOf<String, List<CheckIn>>()

    override suspend fun getAll(
        locationId: Int,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<CheckIn>> {
        if (forceRefresh) checkInsCache.clear()

        val key = "$locationId-$page"
        val cache = checkInsCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getAll(locationId = locationId, page = page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { checkInsCache[key] = it }
    }

    override suspend fun getHistory(
        userId: Int,
        userType: UserType,
        page: Int,
        forceRefresh: Boolean,
    ): DomainResult<List<CheckIn>> {
        if (forceRefresh) checkInsForUserCache.clear()

        val key = "$userId-$userType-$page"
        val cache = checkInsForUserCache[key]
        if (!cache.isNullOrEmpty()) return DomainResult.success(cache)

        return helper.getHistory(userId = userId, userType = userType.type, page = page)
            .toDomainResult()
            .map { dtos -> dtos.map { it.toModel() } }
            .onSuccess { checkInsForUserCache[key] = it }
    }

    override suspend fun get(userId: Int, userType: UserType): DomainResult<CheckIn> {
        checkInsCache.values.flatten().firstOrNull { it.userId == userId && it.userType == userType.type }?.let {
            return DomainResult.success(it)
        }

        checkInsForUserCache.values.flatten().firstOrNull { it.userId == userId && it.userType == userType.type }?.let {
            return DomainResult.success(it)
        }

        return helper.getCheckIn(userId, userType.type).toDomainResult().map { it.toModel() }
    }

    override suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: UserType,
    ): DomainResult<Unit> {
        return helper.checkIn(
            locationId,
            userId,
            userType.type,
        ).toDomainResult()
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String): DomainResult<Unit> {
        return helper.updatePrivacy(checkInId, privacy).toDomainResult()
    }

    override suspend fun delete(checkInId: Int): DomainResult<Unit> {
        return helper.delete(checkInId).toDomainResult()
    }

    override fun clearCache() {
        checkInsCache.clear()
        checkInsForUserCache.clear()
    }
}