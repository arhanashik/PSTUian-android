package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface CheckInRepository {

    suspend fun getAll(
        locationId: Int,
        page: Int,
        forceRefresh: Boolean = false,
    ) : DomainResult<List<CheckIn>>

    suspend fun getHistory(
        userId: Int,
        userType: UserType,
        page: Int,
        forceRefresh: Boolean = false,
    ) : DomainResult<List<CheckIn>>

    suspend fun get(userId: Int, userType: UserType): DomainResult<CheckIn>

    suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: UserType,
    ): DomainResult<Unit>

    suspend fun updatePrivacy(checkInId: Int, privacy: String): DomainResult<CheckIn>

    suspend fun delete(checkInId: Int) : DomainResult<Unit>

    fun clearCache()
}