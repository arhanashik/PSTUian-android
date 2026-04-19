package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.AppConfig
import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.isVersionLower

class GetInitialScreenUseCase(private val platformInfo: PlatformInfo) {

    operator fun invoke(
        device: Device?,
        appConfig: AppConfig?,
    ): InitialScreenState {
        if (device == null || appConfig == null) return InitialScreenState.MISSING_CONFIG
        if (device.blocklisted) return InitialScreenState.BLOCKLISTED
        if (appConfig.maintenance) return InitialScreenState.MAINTENANCE

        val needForceUpdate = isVersionLower(
            current = platformInfo.appVersionName,
            required = appConfig.forceUpdateVersion,
        )
        if (needForceUpdate) return InitialScreenState.FORCE_UPDATE

        return InitialScreenState.HOME
    }
}

enum class InitialScreenState {
    MISSING_CONFIG,
    BLOCKLISTED,
    FORCE_UPDATE,
    MAINTENANCE,
//    SIGN_IN,
//    SIGN_IN_AGAIN,
//    EMAIL_VERIFICATION,
    HOME,
}
