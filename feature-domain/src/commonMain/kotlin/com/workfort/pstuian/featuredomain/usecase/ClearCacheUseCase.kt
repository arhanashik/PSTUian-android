package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.repository.FacultyRepository

class ClearCacheUseCase(private val facultyRepo: FacultyRepository) {
    suspend operator fun invoke() {
        facultyRepo.clearCache()
    }
}
