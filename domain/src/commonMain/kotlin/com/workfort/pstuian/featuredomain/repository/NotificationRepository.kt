package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.NotificationEntity
import com.workfort.pstuian.featuredomain.model.UserType

interface NotificationRepository {

    suspend fun getAll(
        userType: UserType,
        page: Int,
    ): List<NotificationEntity>
}