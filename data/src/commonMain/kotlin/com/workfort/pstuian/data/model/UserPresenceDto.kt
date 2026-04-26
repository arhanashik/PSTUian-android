package com.workfort.pstuian.data.model

import com.workfort.pstuian.featuredomain.model.UserPresence
import kotlinx.serialization.Serializable

@Serializable
data class UserPresenceDto(
    val lastSeenAt: Long = 0L,
) {

    fun toModel(userId: String): UserPresence = UserPresence(
        userId = userId,
        lastSeenAt = lastSeenAt,
    )
}