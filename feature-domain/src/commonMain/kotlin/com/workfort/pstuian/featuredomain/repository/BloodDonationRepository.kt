package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.BloodDonationEntity

interface BloodDonationRepository {
    suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int,
    ): List<BloodDonationEntity>

    suspend fun get(id: Int): BloodDonationEntity

    suspend fun insert(
        requestId: Int?,
        date: Long,
        info: String?,
    ) : BloodDonationEntity

    suspend fun update(item: BloodDonationEntity): BloodDonationEntity

    suspend fun delete(id: Int): Boolean
}