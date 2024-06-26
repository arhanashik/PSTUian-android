package com.workfort.pstuian.repository

import com.workfort.pstuian.model.BloodDonationEntity
import com.workfort.pstuian.networking.domain.BloodDonationApiHelper


class BloodDonationRepository(
    private val authRepo: AuthRepository,
    private val helper: BloodDonationApiHelper,
) {
    suspend fun getAll(userId: Int, userType: String, page: Int) =
        helper.getAll(userId, userType, page)
    suspend fun get(id: Int) = helper.get(id)
    suspend fun insert(
        requestId: Int?,
        date: String,
        info: String?,
    ) : BloodDonationEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.insert(userIdAndType.first, userIdAndType.second, requestId, date, info)
            ?: throw Exception("Insert failed")
    }
    suspend fun update(item: BloodDonationEntity) = helper.update(item)
    suspend fun delete(id: Int) = helper.delete(id)
}