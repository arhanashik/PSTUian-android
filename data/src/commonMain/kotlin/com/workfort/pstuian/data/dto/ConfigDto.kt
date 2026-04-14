package com.workfort.pstuian.data.dto

import com.workfort.pstuian.featuredomain.model.ConfigEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigDto(
    val id: Int = 0,
    @SerialName("android_version")
    var androidVersion: String = "",
    @SerialName("ios_version")
    var iosVersion: String = "",
    @SerialName("data_refresh_version")
    var dataRefreshVersion: String = "",
    @SerialName("api_version")
    var apiVersion: String = "",
    @SerialName("admin_api_version")
    var adminApiVersion: String = "",
    @SerialName("force_refresh")
    var forceRefresh: Int = 0,
    @SerialName("force_update")
    var forceUpdate: Int = 0,
) {
    fun toEntity() = ConfigEntity(
        id = id,
        androidVersion = androidVersion,
        iosVersion = iosVersion,
        dataRefreshVersion = dataRefreshVersion,
        apiVersion = apiVersion,
        adminApiVersion = adminApiVersion,
        forceRefresh = forceRefresh,
        forceUpdate = forceUpdate,
    )
}

fun ConfigEntity.toDto() = ConfigDto(
    id = id,
    androidVersion = androidVersion,
    iosVersion = iosVersion,
    dataRefreshVersion = dataRefreshVersion,
    apiVersion = apiVersion,
    adminApiVersion = adminApiVersion,
    forceRefresh = forceRefresh,
    forceUpdate = forceUpdate,
)
