package com.workfort.pstuian.util.deeplink

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordParams(
    val mode: String? = null,
    val oobCode: String? = null,
    val apiKey: String? = null,
    val continueUrl: String? = null,
    val languageCode: String? = null,
    val tenantId: String? = null,
) {
    fun hasAnyValue(): Boolean = listOf(
        mode,
        oobCode,
        apiKey,
        continueUrl,
        languageCode,
        tenantId,
    ).any { !it.isNullOrBlank() }
}
