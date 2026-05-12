package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPresence(
    val presenceId: String,
    val sessionStartedAt: Long = 0L,
)