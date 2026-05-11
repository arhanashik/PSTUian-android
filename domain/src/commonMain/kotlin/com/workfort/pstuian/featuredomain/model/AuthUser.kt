package com.workfort.pstuian.featuredomain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
    val userId: String,
    val email: String,
)
