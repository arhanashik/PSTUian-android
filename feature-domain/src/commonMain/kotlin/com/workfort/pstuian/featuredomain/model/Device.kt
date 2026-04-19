package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class Device(
    val deviceId: String,
    val model: String,
    val platform: String,
    val appVersionCode: Int,
    val appVersionName: String,
    val fcmToken: String?,
    val blocklisted: Boolean,
    val lat: String?,
    val lng: String?,
    val locale: String,
    val createdAt: String,
    val updatedAt: String,
)
