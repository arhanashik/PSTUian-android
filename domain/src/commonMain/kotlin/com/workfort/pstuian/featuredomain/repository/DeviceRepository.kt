package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserType

interface DeviceRepository {

    suspend fun getAllDevices(
        userId: String,
        userType: UserType,
        page: Int,
        useCache: Boolean = true,
    ): DomainResult<List<Device>>

    suspend fun registerDevice(
        deviceId: String,
        model: String,
        platform: String,
        appVersionCode: Int,
        appVersionName: String,
        fcmToken: String?,
        lat: String?,
        lng: String?,
        locale: String,
    ): DomainResult<Device>

    fun clearCache()
}