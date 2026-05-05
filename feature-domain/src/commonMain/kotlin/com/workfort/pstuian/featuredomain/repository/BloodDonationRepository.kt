package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface BloodDonationRepository {

    suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
        forceRefresh: Boolean = false,
    ): DomainResult<List<BloodDonationEntity>>

    suspend fun get(id: Int): DomainResult<BloodDonationEntity>

    suspend fun insert(
        requestId: Int,
        userId: Int,
        userType: UserType,
        date: String,
        info: String,
    ) : DomainResult<BloodDonationEntity>

    suspend fun update(item: BloodDonationEntity): DomainResult<Unit>

    suspend fun delete(id: Int): DomainResult<Unit>
}