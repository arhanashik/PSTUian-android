package com.workfort.pstuian.app.ui.common.infrastructure.usecase

import com.workfort.pstuian.app.ui.common.domain.usecase.RegisterDeviceUseCase
import com.workfort.pstuian.firebase.fcm.FcmUtil
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.repository.AuthRepository

class RegisterDeviceUseCaseImpl(
    private val authRepository: AuthRepository,
) : RegisterDeviceUseCase {
    override suspend fun invoke(): Result<DeviceEntity> {
        return runCatching {
            val fcmToken = FcmUtil.getFcmToken()
            authRepository.registerDevice(fcmToken)
        }.onFailure {
            return Result.failure(it)
        }
    }
}