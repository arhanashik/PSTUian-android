package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.DeviceDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class DeviceApiService(private val client: HttpClient) {

    suspend fun getAllDevices(
        userId: String,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int,
    ): ApiResponse<List<DeviceDto>> {
        return client.get(NetworkConst.Remote.Api.Device.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.DEVICE_ID, deviceId)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun registerDevice(
        deviceId: String,
        model: String,
        platform: String,
        appVersionCode: String,
        appVersionName: String,
        fcmToken: String,
        lat: String,
        lng: String,
        locale: String,
    ): ApiResponse<DeviceDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Device.REGISTER,
            formParameters = parameters {
                append(NetworkConst.Params.DEVICE_ID, deviceId)
                append(NetworkConst.Params.MODEL, model)
                append(NetworkConst.Params.PLATFORM, platform)
                append(NetworkConst.Params.APP_VERSION_CODE, appVersionCode)
                append(NetworkConst.Params.APP_VERSION_NAME, appVersionName)
                append(NetworkConst.Params.FCM_TOKEN, fcmToken)
                append(NetworkConst.Params.LAT, lat)
                append(NetworkConst.Params.LNG, lng)
                append(NetworkConst.Params.LOCALE, locale)
            }
        ).body()
    }
}
