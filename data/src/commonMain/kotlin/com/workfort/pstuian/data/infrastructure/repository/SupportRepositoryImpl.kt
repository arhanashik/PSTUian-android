package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.SupportApiHelper
import com.workfort.pstuian.featuredomain.repository.SupportRepository

class SupportRepositoryImpl(private val helper: SupportApiHelper) : SupportRepository {
    override suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String,
    ): String {
        return helper.sendInquiry(name, email, type, query)
    }
}