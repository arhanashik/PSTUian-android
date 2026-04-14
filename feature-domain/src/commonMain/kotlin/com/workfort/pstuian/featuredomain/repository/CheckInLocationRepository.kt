package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.CheckInLocationEntity

interface CheckInLocationRepository {
    suspend fun getAll(page: Int) : List<CheckInLocationEntity>
    suspend fun get(id: Int) : CheckInLocationEntity
    suspend fun search(query: String, page: Int) : List<CheckInLocationEntity>
    suspend fun insert(
        name: String,
        details: String? = "",
        imageUrl: String? = "",
        link: String? = "",
    ): CheckInLocationEntity
}