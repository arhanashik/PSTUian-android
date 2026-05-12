package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface CheckInLocationRepository {

    suspend fun getAll(page: Int, forceRefresh: Boolean): DomainResult<List<CheckInLocation>>

    suspend fun get(id: Int): DomainResult<CheckInLocation>

    suspend fun search(query: String, page: Int): DomainResult<List<CheckInLocation>>

    suspend fun insert(
        userType: UserType,
        name: String,
        details: String? = "",
        imageUrl: String? = "",
        link: String? = "",
    ): DomainResult<Unit>
}