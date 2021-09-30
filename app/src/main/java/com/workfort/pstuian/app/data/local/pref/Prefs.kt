package com.workfort.pstuian.app.data.local.pref

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

    var donateOption: String?
        get() = prefs.getString(keyDonateOption, "")
        set(value) = prefs.edit { it.putString(keyDonateOption, value) }

    var donationId: String?
        get() = prefs.getString(keyDonationId, "")
        set(value) = prefs.edit { it.putString(keyDonationId, value) }

    fun clear() {
        prefs.edit().clear().apply()
    }
}