package com.workfort.pstuian.app.ui.common.domain.usecase

import com.workfort.pstuian.model.DeviceEntity

interface RegisterDeviceUseCase {
    suspend operator fun invoke(): Result<DeviceEntity>
}