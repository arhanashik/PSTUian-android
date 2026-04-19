package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.model.toDto
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository

class BloodDonationRepositoryImpl(
    private val helper: BloodDonationApiHelper,
) : BloodDonationRepository {

    override suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
    ): List<BloodDonationEntity> {
        return helper.getAll(userId, userType, page).map { it.toEntity() }
    }

    override suspend fun get(id: Int) = helper.get(id).toEntity()

    override suspend fun insert(
        requestId: Int?,
        userId: String,
        userType: UserType,
        date: Long,
        info: String?,
    ) : BloodDonationEntity {
        return helper.insert(userId, userType.type, requestId, date, info)
            ?.toEntity() ?: throw Exception("Insert failed")
    }

    override suspend fun update(item: BloodDonationEntity) = helper.update(item.toDto()).toEntity()

    override suspend fun delete(id: Int) = helper.delete(id)
}