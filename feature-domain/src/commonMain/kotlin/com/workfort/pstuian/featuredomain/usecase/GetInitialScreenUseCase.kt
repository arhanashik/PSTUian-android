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
        if (device == null) return InitialScreenState.MissingDeviceInfo
        if (device.blocklisted) return InitialScreenState.DeviceBlocklisted

        if (appConfig == null) return InitialScreenState.MissingConfig
        if (appConfig.maintenance) return InitialScreenState.Maintenance(
            remainingTime = appConfig.remainingMaintenance,
        )

        val needForceUpdate = isVersionLower(
            current = platformInfo.appVersionName,
            required = appConfig.forceUpdateVersion,
        )
        if (needForceUpdate) return InitialScreenState.ForceUpdate

        return InitialScreenState.Home
    }
}

sealed interface InitialScreenState {
    data object MissingDeviceInfo : InitialScreenState
    data object DeviceBlocklisted : InitialScreenState
    data object MissingConfig : InitialScreenState
    data object ForceUpdate : InitialScreenState
    data class Maintenance(val remainingTime: Double) : InitialScreenState
    data object Home : InitialScreenState
}
