package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.AuthUser
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserDto(
    val userId: String,
    val email: String,
) {
    fun toAuthUser() = AuthUser(
        userId = userId,
        email = email,
    )
}