package com.workfort.pstuian.featuredomain.repository

interface SettingsRepository {
    fun shouldShowNotification(): Boolean

    suspend fun setShowNotification(show: Boolean)
}
