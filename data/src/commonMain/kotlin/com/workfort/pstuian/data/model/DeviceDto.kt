package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Device
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val deviceId: String = "",
    val model: String = "",
    val platform: String = "",
    val appVersionCode: Int = 0,
    val appVersionName: String = "",
    val fcmToken: String? = null,
    val blocklisted: Boolean = false,
    val lat: String? = null,
    val lng: String? = null,
    val locale: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
) {
    fun toModel() = Device(
        deviceId = deviceId,
        model = model,
        platform = platform,
        appVersionCode = appVersionCode,
        appVersionName = appVersionName,
        fcmToken = fcmToken,
        blocklisted = blocklisted,
        lat = lat,
        lng = lng,
        locale = locale,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun Device.toDto() = DeviceDto(
    deviceId = deviceId,
    model = model,
    platform = platform,
    appVersionCode = appVersionCode,
    appVersionName = appVersionName,
    fcmToken = fcmToken,
    blocklisted = blocklisted,
    lat = lat,
    lng = lng,
    locale = locale,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
