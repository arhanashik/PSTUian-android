package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity

interface BloodDonationRequestRepository {
    suspend fun getAll(page: Int) : List<BloodDonationRequestEntity>
    suspend fun get(id: Int) : BloodDonationRequestEntity
    suspend fun insert(
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): BloodDonationRequestEntity

    suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) : BloodDonationRequestEntity

    suspend fun delete(id: Int) : Boolean
}