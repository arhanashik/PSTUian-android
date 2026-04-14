package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.util.PushNotificationProvider

class RegisterDeviceUseCase(
    private val authRepository: AuthRepository,
    private val pushNotificationRepository: PushNotificationProvider,
) {
    suspend operator fun invoke(): Result<DeviceEntity> {
        return runCatching {
            val fcmToken = pushNotificationRepository.getPushToken() ?: ""
            authRepository.registerDevice(fcmToken)
        }.onFailure {
            return Result.failure(it)
        }
    }
}
