package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.local.keyvaluestorage.Prefs
import com.workfort.pstuian.featuredomain.repository.SettingsRepository

class SettingsRepositoryImpl(private val prefs: Prefs) : SettingsRepository {
    override fun shouldShowNotification(): Boolean {
        return prefs.showNotification
    }

    override suspend fun setShowNotification(show: Boolean) {
        prefs.showNotification = show
    }
}
