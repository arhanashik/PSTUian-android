package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.NotificationEntity

interface NotificationRepository {
    suspend fun getAll(page: Int): List<NotificationEntity>
}