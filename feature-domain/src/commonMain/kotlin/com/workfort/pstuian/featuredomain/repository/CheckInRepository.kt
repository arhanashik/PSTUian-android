package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.UserType

interface CheckInRepository {
    suspend fun getAll(locationId: Int, page: Int) : List<CheckInEntity>

    suspend fun getAll(userId: String, userType: UserType, page: Int) : List<CheckInEntity>

    suspend fun getMyCheckIn(userId: String, userType: UserType): CheckInEntity

    suspend fun checkIn(
        userId: String,
        userType: UserType,
        locationId: Int,
    ): CheckInEntity

    suspend fun updatePrivacy(checkInId: Int, privacy: String): CheckInEntity

    suspend fun delete(checkInId: Int) : Boolean
}