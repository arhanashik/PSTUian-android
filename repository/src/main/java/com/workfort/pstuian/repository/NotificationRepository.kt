package com.workfort.pstuian.repository

import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.networking.domain.NotificationApiHelper


class NotificationRepository(
    private val helper: NotificationApiHelper,
    private val authRepo: AuthRepository
) {
    suspend fun getAll(page: Int): List<NotificationEntity> {
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
        return helper.getAll(userId, userType, page, limit = 20)
    }
}