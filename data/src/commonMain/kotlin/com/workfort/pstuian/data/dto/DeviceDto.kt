package com.workfort.pstuian.data.dto

import com.workfort.pstuian.featuredomain.model.DeviceEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    var id: String,
    @SerialName("fcm_token")
    var fcmToken: String,
    var model: String? = "",
    @SerialName("android_version")
    var androidVersion: String? = "",
    @SerialName("app_version_code")
    var appVersionCode: Int? = 0,
    @SerialName("app_version_name")
    var appVersionName: String? = "",
    @SerialName("ip_address")
    var ipAddress: String? = "",
    var lat: String? = "",
    var lng: String? = "",
    var locale: String? = "",
    @SerialName("created_at")
    var createdAt: String? = "",
    @SerialName("updated_at")
    var updatedAt: String? = "",
) {
    fun toEntity() = DeviceEntity(
        id = id,
        fcmToken = fcmToken,
        model = model,
        androidVersion = androidVersion,
        appVersionCode = appVersionCode,
        appVersionName = appVersionName,
        ipAddress = ipAddress,
        lat = lat,
        lng = lng,
        locale = locale,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun DeviceEntity.toDto() = DeviceDto(
    id = id,
    fcmToken = fcmToken,
    model = model,
    androidVersion = androidVersion,
    appVersionCode = appVersionCode,
    appVersionName = appVersionName,
    ipAddress = ipAddress,
    lat = lat,
    lng = lng,
    locale = locale,
    createdAt = createdAt,
    updatedAt = updatedAt
)
