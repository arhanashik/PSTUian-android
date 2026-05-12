package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.featuredomain.model.NotificationEntity
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val helper: NotificationApiHelper,
) : NotificationRepository {

    override suspend fun getAll(
        userType: UserType,
        page: Int,
    ): List<NotificationEntity> {
        return helper.getAll(userType.type, page).map { it.toEntity() }
    }
}