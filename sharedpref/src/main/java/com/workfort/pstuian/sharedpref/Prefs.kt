package com.workfort.pstuian.sharedpref

import android.content.Context
import android.content.SharedPreferences


object Prefs : PrefProp {
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        get() = prefs.getBoolean(keyFirstRun, false)
        set(value) = prefs.edit { it.putBoolean(keyFirstRun, value) }

    var deviceId: String?
        get() = prefs.getString(keyDeviceId, "")
        set(value) = prefs.edit { it.putString(keyDeviceId, value) }

    var fcmToken: String?
        get() = prefs.getString(keyFcmToken, "")
        set(value) = prefs.edit { it.putString(keyFcmToken, value) }

    var authToken: String?
        get() = prefs.getString(keyAuthToken, "")
        set(value) = prefs.edit { it.putString(keyAuthToken, value) }

    var donateOption: String?
        get() = prefs.getString(keyDonateOption, "")
        set(value) = prefs.edit { it.putString(keyDonateOption, value) }

    var donationId: String?
        get() = prefs.getString(keyDonationId, "")
        set(value) = prefs.edit { it.putString(keyDonationId, value) }

    var hasNewNotification: Boolean
        get() = prefs.getBoolean(keyHasNewNotification, false)
        set(value) = prefs.edit { it.putBoolean(keyHasNewNotification, value) }

    var lastShownCheckInLocationId: Int
        get() = prefs.getInt(keyLastShownCheckInLocationId, -1)
        set(value) = prefs.edit { it.putInt(keyLastShownCheckInLocationId, value) }

    fun clear() {
        prefs.edit().clear().apply()
    }
}