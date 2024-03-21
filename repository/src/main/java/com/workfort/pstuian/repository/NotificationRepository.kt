package com.workfort.pstuian.repository

import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.networking.NotificationApiHelper

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:01 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

class NotificationRepository(
    private val helper: com.workfort.pstuian.networking.NotificationApiHelper,
    private val authRepo: AuthRepository
) {
    suspend fun getAll(page: Int): List<NotificationEntity> {
        var userId = -1
        var userType = ""
        try {
            userId = when(val user = authRepo.getSignInUser()) {
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