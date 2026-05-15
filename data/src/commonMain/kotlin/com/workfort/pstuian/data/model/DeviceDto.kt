package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.Device
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val id: Int = 0,
    @SerialName("device_id")
    val deviceId: String = "",
    val model: String = "",
    val platform: String = "",
    @SerialName("app_version_code")
    val appVersionCode: Int = 0,
    @SerialName("app_version_name")
    val appVersionName: String = "",
    @SerialName("fcm_token")
    val fcmToken: String? = null,
    val lat: String? = null,
    val lng: String? = null,
    val locale: String = "",
    val blocklisted: Boolean = false,
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("updated_at")
    val updatedAt: String = "",
) {
    fun toModel() = Device(
        id = id,
        deviceId = deviceId,
        model = model,
        platform = platform,
        appVersionCode = appVersionCode,
        appVersionName = appVersionName,
        fcmToken = fcmToken,
        lat = lat,
        lng = lng,
        locale = locale,
        blocklisted = blocklisted,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun Device.toDto() = DeviceDto(
    id = id,
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
