package com.workfort.pstuian.app.data.repository

import com.workfort.pstuian.app.data.local.notification.NotificationEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.remote.apihelper.NotificationApiHelper
import timber.log.Timber

/**
 *  ****************************************************************************
 *  * Created by : arhan on 29 Oct, 2021 at 21:01 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/10/29.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class NotificationRepository(
    private val helper: NotificationApiHelper,
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
            Timber.e("Getting Notification for non signed in user")
        }
        return helper.getAll(userId, userType, page, limit = 20)
    }
}