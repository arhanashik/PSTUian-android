package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.BloodDonationRequestApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.BloodDonationRequestRepository

class BloodDonationRequestRepositoryImpl(
    private val authRepo: AuthRepository,
    private val helper: BloodDonationRequestApiHelper,
) : BloodDonationRequestRepository {
    override suspend fun getAll(page: Int) =
        helper.getAll(page, limit = 20).map { it.toEntity() }

    override suspend fun get(id: Int) = helper.get(id).toEntity()

    override suspend fun insert(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestEntity {
        val userIdAndType = authRepo.getUserIdAndType()

        return helper.insert(
            userIdAndType.first,
            userIdAndType.second,
            bloodGroup,
            beforeDate,
            contact,
            info
        ).toEntity()
    }

    override suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) = helper.update(id, bloodGroup, beforeDate, contact, info).toEntity()

    override suspend fun delete(id: Int) = helper.delete(id)
}