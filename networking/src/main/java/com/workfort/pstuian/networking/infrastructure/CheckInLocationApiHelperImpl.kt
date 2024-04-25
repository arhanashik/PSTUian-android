package com.workfort.pstuian.networking.infrastructure

import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.networking.domain.CheckInLocationApiHelper
import com.workfort.pstuian.networking.service.CheckInLocationApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 21:31.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */
class CheckInLocationApiHelperImpl(
    private val service: CheckInLocationApiService
) : CheckInLocationApiHelper() {
    override suspend fun getAll(page: Int, limit: Int): List<CheckInLocationEntity> {
        service.getAll(page, limit).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun get(id: Int): CheckInLocationEntity {
        service.get(id).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun search(
        query: String,
        page: Int,
        limit: Int
    ): List<CheckInLocationEntity> {
        service.search(query, page, limit).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }

    override suspend fun insert(
        userId: Int,
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?
    ): CheckInLocationEntity {
        service.insert(userId, userType, name, details, imageUrl, link).also {
            if(!it.success) throw Exception(it.message)
            return it.data?: throw Exception("No data")
        }
    }
}