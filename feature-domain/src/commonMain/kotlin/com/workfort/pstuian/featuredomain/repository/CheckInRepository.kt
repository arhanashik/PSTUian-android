package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface CheckInRepository {
    suspend fun getAll(locationId: Int, page: Int) : DomainResult<List<CheckIn>>

    suspend fun getAll(userId: String, userType: UserType, page: Int) : DomainResult<List<CheckIn>>

    suspend fun getMyCheckIn(userId: String, userType: UserType): DomainResult<CheckIn>

    suspend fun checkIn(
        userId: String,
        userType: UserType,
        locationId: Int,
    ): DomainResult<CheckIn>

    suspend fun updatePrivacy(checkInId: Int, privacy: String): DomainResult<CheckIn>

    suspend fun delete(checkInId: Int) : DomainResult<Unit>
}