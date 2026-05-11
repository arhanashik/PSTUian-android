package com.workfort.pstuian.ui.profile.common.displaydata

import kotlinx.serialization.Serializable

@Serializable
data class UserPresenceDisplayData(
    val isOnline: Boolean = false,
    val formatedLastSeenAt: String = "",
)