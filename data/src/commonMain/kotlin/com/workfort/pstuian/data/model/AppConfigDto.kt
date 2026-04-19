package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.AppConfig
import kotlinx.serialization.Serializable

@Serializable
data class AppConfigDto(
    val maintenance: Boolean = false,
    var apiVersion: String = "1.0.0",
    var adminApiVersion: String = "1.0.0",
    val forceUpdateVersion: String = "1.0.0",
    val privacyPolicyUrl: String = "",
    val termsAndConditionsUrl: String = "",
    val contactUrl: String = "",
    val deleteAccountUrl: String = "",
) {
    fun toModel(id: String) = AppConfig(
        id = id,
        maintenance = maintenance,
        apiVersion = apiVersion,
        adminApiVersion = adminApiVersion,
        forceUpdateVersion = forceUpdateVersion,
        privacyPolicyUrl = privacyPolicyUrl,
        termsAndConditionsUrl = termsAndConditionsUrl,
        contactUrl = contactUrl,
        deleteAccountUrl = deleteAccountUrl,
    )
}

fun AppConfig.toDto() = AppConfigDto(
    maintenance = maintenance,
    apiVersion = apiVersion,
    adminApiVersion = adminApiVersion,
    forceUpdateVersion = forceUpdateVersion,
    privacyPolicyUrl = privacyPolicyUrl,
    termsAndConditionsUrl = termsAndConditionsUrl,
    contactUrl = contactUrl,
    deleteAccountUrl = deleteAccountUrl,
)
