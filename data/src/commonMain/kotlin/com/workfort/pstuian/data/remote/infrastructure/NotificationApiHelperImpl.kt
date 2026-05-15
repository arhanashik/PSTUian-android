package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.CustomNotificationDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.NotificationApiHelper
import com.workfort.pstuian.data.remote.service.CustomNotificationApiService

class NotificationApiHelperImpl(
    private val service: CustomNotificationApiService,
) : NotificationApiHelper {

    override suspend fun getAll(
        userType: String,
        page: Int,
        limit: Int,
    ): NetworkResult<List<CustomNotificationDto>> {
        return runCatching {
            service.getAll(userType, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun hasUnreadCustomNotifications(userType: String): NetworkResult<Boolean> {
        return runCatching {
            when (val result = service.hasUnreadCustomNotifications(userType).toNetworkResult()) {
                is NetworkResult.Success -> NetworkResult.success(result.value.hasUnread)
                is NetworkResult.Failure -> result
            }
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun markCustomNotificationAsRead(notificationId: String): NetworkResult<Unit> {
        return runCatching {
            service.markCustomNotificationAsRead(notificationId).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}
