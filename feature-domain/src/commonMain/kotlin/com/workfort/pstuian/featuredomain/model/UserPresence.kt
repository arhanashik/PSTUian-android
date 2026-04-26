package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPresence(
    val userId: String,
    val lastSeenAt: Long = 0L,
)