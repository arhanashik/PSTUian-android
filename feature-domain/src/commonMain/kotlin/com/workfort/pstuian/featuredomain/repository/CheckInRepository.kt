package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckInEntity

interface CheckInRepository {
    suspend fun getAll(locationId: Int, page: Int) : List<CheckInEntity>

    suspend fun getAll(userId: Int, userType: String, page: Int) : List<CheckInEntity>

    suspend fun getMyCheckIn(): CheckInEntity

    suspend fun checkIn(locationId: Int): CheckInEntity

    suspend fun updatePrivacy(checkInId: Int, privacy: String): CheckInEntity

    suspend fun delete(checkInId: Int) : Boolean
}