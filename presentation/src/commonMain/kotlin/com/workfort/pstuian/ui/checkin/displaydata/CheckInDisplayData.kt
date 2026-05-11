package com.workfort.pstuian.ui.checkin.displaydata

import com.workfort.pstuian.featuredomain.model.CheckIn
import kotlinx.serialization.Serializable

@Serializable
data class CheckInDisplayData(
    val checkIn: CheckIn,
    val isOnline: Boolean,
)