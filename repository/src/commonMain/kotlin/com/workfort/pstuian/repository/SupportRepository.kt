package com.workfort.pstuian.repository

import com.workfort.pstuian.networking.domain.SupportApiHelper


class SupportRepository(private val helper: SupportApiHelper) {
    suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String,
    ): String {
        return helper.sendInquiry(name, email, type, query)
    }
}