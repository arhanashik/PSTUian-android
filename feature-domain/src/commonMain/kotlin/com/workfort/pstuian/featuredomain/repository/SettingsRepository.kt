package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun shouldShowNotification(): Boolean
    fun showNotification(show: Boolean)
    fun getTheme(): ThemeMode
    fun setTheme(theme: ThemeMode)
    fun observeTheme(): Flow<ThemeMode>
    fun getFcmToken(): String?
    fun setFcmToken(fcmToken: String)
    fun clearSharedPrefs()
}