package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.BloodDonationRequestEntity
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface BloodDonationRequestRepository {
    suspend fun getAll(page: Int, forceRefresh: Boolean = false) : DomainResult<List<BloodDonationRequestEntity>>
    suspend fun get(id: Int) : BloodDonationRequestEntity
    suspend fun insert(
        userId: String,
        userType: UserType,
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