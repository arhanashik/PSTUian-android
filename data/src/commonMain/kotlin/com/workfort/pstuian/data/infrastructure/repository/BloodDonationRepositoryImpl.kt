package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.model.toDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.BloodDonationApiHelper
import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.BloodDonationRepository

class BloodDonationRepositoryImpl(
    private val helper: BloodDonationApiHelper,
) : BloodDonationRepository {

    override suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
    ): List<BloodDonationEntity> {
        return when (val result = helper.getAll(userId, userType, page)) {
            is NetworkResult.Success -> result.value.map { it.toEntity() }
            is NetworkResult.Failure -> throw result.error
        }
    }

    override suspend fun get(id: Int) = when (val result = helper.get(id)) {
        is NetworkResult.Success -> result.value.toEntity()
        is NetworkResult.Failure -> throw result.error
    }

    override suspend fun insert(
        requestId: Int?,
        userId: Int,
        userType: UserType,
        date: Long,
        info: String?,
    ) : BloodDonationEntity {
        return when (val result = helper.insert(userId, userType.type, requestId, date, info)) {
            is NetworkResult.Success -> result.value.toEntity()
            is NetworkResult.Failure -> throw result.error
        }
    }

    override suspend fun update(item: BloodDonationEntity) = when (val result = helper.update(item.toDto())) {
        is NetworkResult.Success -> result.value.toEntity()
        is NetworkResult.Failure -> throw result.error
    }

    override suspend fun delete(id: Int) = when (helper.delete(id)) {
        is NetworkResult.Success -> true
        is NetworkResult.Failure -> false
    }
}