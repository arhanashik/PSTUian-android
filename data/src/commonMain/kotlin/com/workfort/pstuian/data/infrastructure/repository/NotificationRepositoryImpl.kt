package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.featuredomain.model.NotificationEntity
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.NotificationRepository


class NotificationRepositoryImpl(
    private val helper: NotificationApiHelper,
    private val authRepo: AuthRepository
) : NotificationRepository {
    override suspend fun getAll(page: Int): List<NotificationEntity> {
        var userId = -1
        var userType = ""
        try {
            userId = when (val user = authRepo.getSignInUser()) {
                is StudentEntity -> user.id
                is TeacherEntity -> user.id
                else -> -1
            }
            userType = authRepo.getSignInUserType()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return helper.getAll(userId, userType, page, limit = 20).map { it.toEntity() }
    }
}