package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NotificationDto
import com.workfort.pstuian.data.remote.NetworkConst

interface NotificationApiHelper {

    suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): List<NotificationDto>
}