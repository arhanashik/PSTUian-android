package com.workfort.pstuian.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.workfort.pstuian.PstuianApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class SettingsRepository {

    private val context = PstuianApp.getBaseApplicationContext()
    private val Context.settingsStore: DataStore<Preferences> by preferencesDataStore(
        SETTINGS_PREFERENCES,
    )

    companion object {
        private const val SETTINGS_PREFERENCES = "settings_preferences"
        private val SHOW_NOTIFICATION = booleanPreferencesKey("show_notification")
    }

    fun shouldShowNotification(defaultValue: Boolean = true): Boolean {
        return runBlocking {
            context.settingsStore.data.first()
        }[SHOW_NOTIFICATION] ?: defaultValue
    }

    suspend fun setShowNotification(show: Boolean) {
        context.settingsStore.edit { settings ->
            settings[SHOW_NOTIFICATION] = show
        }
    }
}