package com.workfort.pstuian.repository

import com.workfort.pstuian.sharedpref.Prefs

class SettingsRepository(private val prefs: Prefs) {
    fun shouldShowNotification(): Boolean {
        return prefs.showNotification
    }

    suspend fun setShowNotification(show: Boolean) {
        prefs.showNotification = show
    }
}
