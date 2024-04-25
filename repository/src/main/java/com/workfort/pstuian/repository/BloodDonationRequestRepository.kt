package com.workfort.pstuian.repository

import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.networking.domain.BloodDonationRequestApiHelper


class BloodDonationRequestRepository(
    private val authRepo: AuthRepository,
    private val helper: BloodDonationRequestApiHelper,
) {
    suspend fun getAll(page: Int) = helper.getAll(page, limit = 20)
    suspend fun get(id: Int) = helper.get(id)
    suspend fun insert(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.insert(
            userIdAndType.first, userIdAndType.second, bloodGroup,
            beforeDate, contact, info
        )
    }

    suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) = helper.update(id, bloodGroup, beforeDate, contact, info)

    suspend fun delete(id: Int) = helper.delete(id)
}