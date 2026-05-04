package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.BloodDonationRequest
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface BloodDonationRequestRepository {
    suspend fun getAll(
        userId: Int,
        userType: UserType,
        page: Int,
        forceRefresh: Boolean = false,
    ) : DomainResult<List<BloodDonationRequest>>
    suspend fun get(id: Int) : BloodDonationRequest
    suspend fun insert(
        userId: Int,
        userType: UserType,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): DomainResult<Unit>

    suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ) : DomainResult<Unit>

    suspend fun delete(id: Int) : DomainResult<Unit>
}