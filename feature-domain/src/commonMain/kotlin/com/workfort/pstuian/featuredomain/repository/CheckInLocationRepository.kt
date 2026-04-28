package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckInLocation
import com.workfort.pstuian.featuredomain.model.UserType

interface CheckInLocationRepository {
    suspend fun getAll(page: Int) : List<CheckInLocation>
    suspend fun get(id: Int) : CheckInLocation
    suspend fun search(query: String, page: Int) : List<CheckInLocation>
    suspend fun insert(
        userId: String,
        userType: UserType,
        name: String,
        details: String? = "",
        imageUrl: String? = "",
        link: String? = "",
    ): CheckInLocation
}