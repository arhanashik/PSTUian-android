package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.AppConfig
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import kotlinx.serialization.Serializable

@Serializable
data class AppConfigDto(
    val maintenance: Boolean = false,
    val maintenanceUntil: Timestamp? = null,
    var apiVersion: String = "1.0.0",
    var adminApiVersion: String = "1.0.0",
    val forceUpdateVersion: String = "1.0.0",
    val privacyPolicyUrl: String = "",
    val termsAndConditionsUrl: String = "",
    val contactUrl: String = "",
    val deleteAccountUrl: String = "",
) {
    fun toModel(id: String, remainingMaintenance: Double = 0.0) = AppConfig(
        id = id,
        maintenance = maintenance,
        remainingMaintenance = remainingMaintenance,
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
    maintenanceUntil = Timestamp.fromMilliseconds(remainingMaintenance),
    apiVersion = apiVersion,
    adminApiVersion = adminApiVersion,
    forceUpdateVersion = forceUpdateVersion,
    privacyPolicyUrl = privacyPolicyUrl,
    termsAndConditionsUrl = termsAndConditionsUrl,
    contactUrl = contactUrl,
    deleteAccountUrl = deleteAccountUrl,
)
