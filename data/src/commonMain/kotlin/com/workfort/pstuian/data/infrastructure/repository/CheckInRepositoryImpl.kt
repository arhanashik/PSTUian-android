package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository

class CheckInRepositoryImpl(
    private val authRepo: AuthRepository,
    private val helper: CheckInApiHelper,
) : CheckInRepository {
    override suspend fun getAll(locationId: Int, page: Int) =
        helper.getAll(locationId = locationId, page = page).map { it.toEntity() }

    override suspend fun getAll(userId: String, userType: UserType, page: Int) =
        helper.getAll(userId = userId, userType = userType.type, page = page).map { it.toEntity() }

    override suspend fun getMyCheckIn(userId: String, userType: UserType): CheckInEntity {
        return helper.getMyCheckIn(
            userId,
            userType.type,
        )?.toEntity() ?: throw Exception("Failed")
    }

    override suspend fun checkIn(
        userId: String,
        userType: UserType,
        locationId: Int,
    ): CheckInEntity {
        return helper.checkIn(
            locationId,
            userId,
            userType.type,
        )?.toEntity() ?: throw Exception("Check in failed")
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String) =
        helper.updatePrivacy(checkInId, privacy)?.toEntity() ?: throw Exception("Update failed")

    override suspend fun delete(checkInId: Int) = helper.delete(checkInId)
}