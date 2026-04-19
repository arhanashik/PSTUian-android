package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.Device
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.DeviceRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.PushNotificationProvider

class RegisterDeviceUseCase(
    private val platformInfo: PlatformInfo,
    private val deviceRepository: DeviceRepository,
    private val pushNotificationRepository: PushNotificationProvider,
    private val sharedPrefRepository: SharedPrefRepository,
) {
    suspend operator fun invoke(): DomainResult<Device> {
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
            ?: platformInfo.deviceId

        return deviceRepository.registerDevice(
            deviceId = deviceId,
            model = platformInfo.model,
            platform = platformInfo.platform,
            appVersionCode = platformInfo.appVersionCode,
            appVersionName = platformInfo.appVersionName,
            fcmToken = pushNotificationRepository.getPushToken(),
            lat = null, // TODO: add lat and lng
            lng = null, // TODO: add lat and lng
            locale = platformInfo.locale,
        ).onSuccess {
            sharedPrefRepository.putString(SharedPrefKey.DEVICE_ID, deviceId)
        }
    }
}
