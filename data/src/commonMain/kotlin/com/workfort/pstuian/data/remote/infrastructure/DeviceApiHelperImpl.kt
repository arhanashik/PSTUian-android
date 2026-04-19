package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.DeviceDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.DeviceApiHelper
import com.workfort.pstuian.data.remote.service.DeviceApiService


class DeviceApiHelperImpl(private val service: DeviceApiService) : DeviceApiHelper {

    override suspend fun getAllDevices(
        userId: String,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int
    ): NetworkResult<List<DeviceDto>> {
        return runCatching {
            return service.getAllDevices(userId, userType, deviceId, page, limit).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }

    override suspend fun registerDevice(
        deviceId: String,
        model: String,
        platform: String,
        appVersionCode: String,
        appVersionName: String,
        fcmToken: String,
        lat: String,
        lng: String,
        locale: String,
    ): NetworkResult<DeviceDto> {
        return runCatching {
            service.registerDevice(
                deviceId = deviceId,
                model = model,
                platform = platform,
                appVersionCode = appVersionCode,
                appVersionName = appVersionName,
                fcmToken = fcmToken,
                lat = lat,
                lng = lng,
                locale = locale,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.UNKNOWN)
        }
    }
}