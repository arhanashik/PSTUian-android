package com.workfort.pstuian.ui.checkinlist

import com.workfort.pstuian.featuredomain.model.CheckIn
import com.workfort.pstuian.ui.checkinlist.displaydata.CheckInDisplayData

class CheckInDisplayDataMapper {

    fun map(
        checkInList: List<CheckIn>,
        currentUserId: Int,
    ): List<CheckInDisplayData> {
        return checkInList.map { checkIn ->
            val isOnline = if (checkIn.userId == currentUserId) true else {
                // TODO replace with real online status value when API supports it.
                false
            }
            CheckInDisplayData(checkIn, isOnline)
        }
    }
}