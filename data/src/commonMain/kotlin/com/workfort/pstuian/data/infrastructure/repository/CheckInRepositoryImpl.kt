package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.CheckInApiHelper
import com.workfort.pstuian.featuredomain.model.CheckInEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository

class CheckInRepositoryImpl(
    private val authRepo: AuthRepository,
    private val helper: CheckInApiHelper,
) : CheckInRepository {
    override suspend fun getAll(locationId: Int, page: Int) =
        helper.getAll(locationId = locationId, page = page).map { it.toEntity() }

    override suspend fun getAll(userId: Int, userType: String, page: Int) =
        helper.getAll(userId = userId, userType = userType, page = page).map { it.toEntity() }

    override suspend fun getMyCheckIn(): CheckInEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.getMyCheckIn(
            userIdAndType.first,
            userIdAndType.second,
        )?.toEntity() ?: throw Exception("Failed")
    }

    override suspend fun checkIn(locationId: Int): CheckInEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.checkIn(
            locationId,
            userIdAndType.first,
            userIdAndType.second,
        )?.toEntity() ?: throw Exception("Check in failed")
    }

    override suspend fun updatePrivacy(checkInId: Int, privacy: String) =
        helper.updatePrivacy(checkInId, privacy)?.toEntity() ?: throw Exception("Update failed")

    override suspend fun delete(checkInId: Int) = helper.delete(checkInId)
}