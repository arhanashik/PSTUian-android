package com.workfort.pstuian.featuredomain.model

data class AppConfig(
    val id: String,
    val maintenance: Boolean,
    val remainingMaintenance: Double, // remaining maintenance time in mills
    var apiVersion: String,
    var adminApiVersion: String,
    val forceUpdateVersion: String,
    val privacyPolicyUrl: String,
    val termsAndConditionsUrl: String,
    val contactUrl: String,
    val deleteAccountUrl: String,
    val donationOptions: String,
)
