package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.data.model.NotificationDto
import com.workfort.pstuian.data.model.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NotificationApiService(private val client: HttpClient) {

    suspend fun getAll(
        userId: String,
        userType: String,
        page: Int,
        limit: Int,
    ): ApiResponse<List<NotificationDto>> {
        return client.get(NetworkConst.Remote.Api.Notification.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }
}
