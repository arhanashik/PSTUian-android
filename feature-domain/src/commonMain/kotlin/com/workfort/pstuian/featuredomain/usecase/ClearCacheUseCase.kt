package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.CheckInRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository

class ClearCacheUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val checkInRepository: CheckInRepository,
    private val sharedPrefRepository: SharedPrefRepository,
) {
    suspend operator fun invoke() {
        facultyRepo.clearCache()
        checkInRepository.clearCache()
        sharedPrefRepository.clear()
        authRepository.signOut()
    }
}
