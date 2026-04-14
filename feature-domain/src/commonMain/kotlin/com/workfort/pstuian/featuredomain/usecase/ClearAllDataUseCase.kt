package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.SliderRepository

class ClearAllDataUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val sliderRepo: SliderRepository,
) {
    suspend operator fun invoke() {
        authRepository.deleteAll()
        sliderRepo.deleteAll()
        facultyRepo.deleteAll()
        authRepository.updateDataRefreshState()
    }
}
