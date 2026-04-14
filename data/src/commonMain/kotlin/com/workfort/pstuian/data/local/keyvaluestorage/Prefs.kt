package com.workfort.pstuian.data.local.keyvaluestorage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class Prefs(private val settings: Settings) : PrefProp {
    var firstRun: Boolean
        get() = settings[keyFirstRun, false]
        set(value) { settings[keyFirstRun] = value }

    var deviceId: String?
        get() = settings[keyDeviceId, ""]
        set(value) { settings[keyDeviceId] = value }

    var fcmToken: String?
        get() = settings[keyFcmToken, ""]
        set(value) { settings[keyFcmToken] = value }

    var authToken: String?
        get() = settings[keyAuthToken, ""]
        set(value) { settings[keyAuthToken] = value }

    var donationId: String?
        get() = settings[keyDonationId, ""]
        set(value) { settings[keyDonationId] = value }

    var hasNewNotification: Boolean
        get() = settings[keyHasNewNotification, false]
        set(value) { settings[keyHasNewNotification] = value }

    var lastShownCheckInLocationId: Int
        get() = settings[keyLastShownCheckInLocationId, -1]
        set(value) { settings[keyLastShownCheckInLocationId] = value }

    var userType: String?
        get() = settings[keyUserType, ""]
        set(value) { settings[keyUserType] = value }

    var user: String?
        get() = settings[keyUser, ""]
        set(value) { settings[keyUser] = value }

    var showNotification: Boolean
        get() = settings[keyShowNotification, true]
        set(value) { settings[keyShowNotification] = value }

    fun clear() {
        settings.clear()
    }
}
