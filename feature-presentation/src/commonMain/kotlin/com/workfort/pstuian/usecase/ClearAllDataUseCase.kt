package com.workfort.pstuian.usecase

import com.workfort.pstuian.repository.AuthRepository
import com.workfort.pstuian.repository.FacultyRepository
import com.workfort.pstuian.repository.SliderRepository
import com.workfort.pstuian.sharedpref.Prefs

class ClearAllDataUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val sliderRepo: SliderRepository,
    private val prefs: Prefs,
) {
    suspend operator fun invoke() {
        val deviceId = prefs.deviceId
        prefs.clear()
        prefs.deviceId = deviceId

        authRepository.deleteAll()
        sliderRepo.deleteAll()
        facultyRepo.deleteAll()
        authRepository.updateDataRefreshState()
    }
}
