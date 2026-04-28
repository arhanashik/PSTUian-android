package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.remote.domain.SupportApiHelper
import com.workfort.pstuian.data.remote.service.SupportApiService

class SupportApiHelperImpl(private val service: SupportApiService) :
    SupportApiHelper {
    override suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String
    ): String {
        val response = service.sendInquiry(name, email, type, query)
        if(!response.isSuccess) throw Exception(response.message)

        return response.message
    }
}