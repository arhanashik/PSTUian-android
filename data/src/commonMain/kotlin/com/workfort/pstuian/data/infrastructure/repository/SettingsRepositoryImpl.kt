package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.featuredomain.model.DebugApiEnvironment
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.ThemeMode
import com.workfort.pstuian.featuredomain.model.UserType
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

    override fun getUserType(): UserType? {
        val type = sharedPrefRepository.getString(SharedPrefKey.SELECTED_USER_TYPE) ?: return null
        return UserType.fromType(type)
    }

    override fun setUserType(userType: UserType?) {
        sharedPrefRepository.putString(SharedPrefKey.SELECTED_USER_TYPE, userType?.type)
    }

    override fun getDebugApiEnvironment(): DebugApiEnvironment =
        DebugApiEnvironment.fromStorageValue(
            sharedPrefRepository.getString(SharedPrefKey.DEBUG_API_ENVIRONMENT),
        )

    override fun setDebugApiEnvironment(environment: DebugApiEnvironment) {
        sharedPrefRepository.putString(SharedPrefKey.DEBUG_API_ENVIRONMENT, environment.storageValue)
    }
}
