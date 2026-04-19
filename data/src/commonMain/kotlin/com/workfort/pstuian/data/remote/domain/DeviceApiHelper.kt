package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.DeviceDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

interface DeviceApiHelper {
    suspend fun getAllDevices(
        userId: String,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<DeviceDto>>

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
    ): NetworkResult<DeviceDto>
}