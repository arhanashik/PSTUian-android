package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.dto.NotificationDto
import com.workfort.pstuian.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NotificationApiService(private val client: HttpClient) {
    suspend fun getAll(
        userId: Int = -1,
        userType: String = "",
        page: Int = 1,
        limit: Int = 20,
    ): Response<List<NotificationDto>> {
        return client.get(NetworkConst.Remote.Api.Notification.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }
}
