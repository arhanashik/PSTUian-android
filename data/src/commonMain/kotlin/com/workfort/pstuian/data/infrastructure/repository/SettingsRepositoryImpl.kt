package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.featuredomain.model.AppUsageRole
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onStart

class SettingsRepositoryImpl(
    private val sharedPrefRepository: SharedPrefRepository,
) : SettingsRepository {

    private val themeFlow = MutableSharedFlow<ThemeMode>(replay = 1)

    override fun shouldShowNotification(): Boolean {
        return sharedPrefRepository.getBoolean(key = SharedPrefKey.SHOW_NOTIFICATION, defaultValue = true)
    }

    override fun showNotification(show: Boolean) {
        sharedPrefRepository.putBoolean(SharedPrefKey.SHOW_NOTIFICATION, show)
    }

    override fun getTheme(): ThemeMode {
        val themeName = sharedPrefRepository.getString(SharedPrefKey.APP_THEME)
        return ThemeMode.fromName(themeName)
    }

    override fun setTheme(theme: ThemeMode) {
        sharedPrefRepository.putString(SharedPrefKey.APP_THEME, theme.name)
        themeFlow.tryEmit(theme)
    }

    override fun observeTheme(): Flow<ThemeMode> {
        return themeFlow.onStart {
            emit(getTheme())
        }
    }

    override fun getFcmToken(): String? = sharedPrefRepository.getString(SharedPrefKey.FCM_TOKEN)

    override fun setFcmToken(fcmToken: String) {
        sharedPrefRepository.putString(SharedPrefKey.FCM_TOKEN, fcmToken)
    }

    override fun clearSharedPrefs() {
        sharedPrefRepository.clear()
    }

    override fun getAppUsageRole(): AppUsageRole? {
        val raw = sharedPrefRepository.getString(SharedPrefKey.APP_USAGE_ROLE, null)
        return AppUsageRole.fromStorage(raw)
    }

    override fun setAppUsageRole(role: AppUsageRole) {
        sharedPrefRepository.putString(SharedPrefKey.APP_USAGE_ROLE, role.storageValue)
    }
}
