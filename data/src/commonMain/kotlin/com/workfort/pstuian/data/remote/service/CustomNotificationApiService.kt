package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.CustomNotificationDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class CustomNotificationApiService(private val client: HttpClient) {

    suspend fun getAll(
        userType: String,
        page: Int,
        limit: Int,
    ): ApiResponse<List<CustomNotificationDto>> {
        return client.get(NetworkConst.Remote.Api.Notification.GET_ALL) {
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun hasUnreadCustomNotifications(userType: String): ApiResponse<Boolean> {
        return client.get(NetworkConst.Remote.Api.Notification.HAS_UNREAD) {
            parameter(NetworkConst.Params.USER_TYPE, userType)
        }.body()
    }

    suspend fun markCustomNotificationAsRead(notificationId: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Notification.MARK_AS_READ,
            formParameters = parameters {
                append(NetworkConst.Params.ID, notificationId)
            },
        ).body()
    }
}
