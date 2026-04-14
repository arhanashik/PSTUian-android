package com.workfort.pstuian.featuredomain.repository

interface SupportRepository {
    suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String,
    ): String
}