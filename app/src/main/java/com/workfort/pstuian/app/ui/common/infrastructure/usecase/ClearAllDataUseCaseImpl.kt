package com.workfort.pstuian.app.ui.common.infrastructure.usecase

import com.workfort.pstuian.app.ui.common.domain.usecase.ClearAllDataUseCase
import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.SliderRepository
import com.workfort.pstuian.sharedpref.Prefs

class ClearAllDataUseCaseImpl(
    private val authRepository: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val sliderRepo: SliderRepository,
) : ClearAllDataUseCase {
    override suspend fun invoke() {
        /** even if we clear the data the device id must not be removed.
         *   because device id should be generated only once during one installation.
         *   so we ensure the device id.
         */
        val deviceId = Prefs.deviceId
        Prefs.clear()
        Prefs.deviceId = deviceId

        authRepository.deleteAll()
        sliderRepo.deleteAll()
        facultyRepo.deleteAll()
        authRepository.updateDataRefreshState()
    }
}