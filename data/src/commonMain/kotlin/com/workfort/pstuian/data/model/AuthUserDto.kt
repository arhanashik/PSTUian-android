package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.AuthUser
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserDto(
    val email: String,
    val displayName: String,
    val photoUrl: String?,
) {
    fun toAuthUser(userId: String) = AuthUser(
        userId = userId,
        email = email,
        displayName = displayName,
        photoUrl = photoUrl,
    )
}