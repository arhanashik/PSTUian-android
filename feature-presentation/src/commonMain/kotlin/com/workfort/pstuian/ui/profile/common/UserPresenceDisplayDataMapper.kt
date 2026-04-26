package com.workfort.pstuian.ui.profile.common

import com.workfort.pstuian.featuredomain.model.UserPresence
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileHeaderDisplayData
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItem
import com.workfort.pstuian.ui.profile.common.displaydata.ProfileInfoItemAction
import com.workfort.pstuian.ui.profile.common.displaydata.UserPresenceDisplayData
import com.workfort.pstuian.util.DateTimeUtil

class UserPresenceDisplayDataMapper(private val dateTimeUtil: DateTimeUtil) {

    private companion object {
        const val PRESENCE_TIMEOUT_MS = 30_000L
    }

    fun map(userPresence: UserPresence?): UserPresenceDisplayData {
        val lastSeenAt = userPresence?.lastSeenAt ?: 0L
        val isOnline = dateTimeUtil.getTimeInMillisNow() - lastSeenAt <= PRESENCE_TIMEOUT_MS
        val formatedLastSeenAt = if (lastSeenAt == 0L) "" else dateTimeUtil.formatTimeHHSS(lastSeenAt)

        return UserPresenceDisplayData(isOnline, formatedLastSeenAt)
    }
}
