package com.workfort.pstuian.usecase

import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.repository.AuthRepository

expect object FcmTokenProvider {
    suspend fun getFcmToken(): String
}

class RegisterDeviceUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<DeviceEntity> {
        return runCatching {
            val fcmToken = FcmTokenProvider.getFcmToken()
            authRepository.registerDevice(fcmToken)
        }.onFailure {
            return Result.failure(it)
        }
    }
}
