package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SettingsRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository

class GetSignedInUserUseCase(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val teacherRepository: TeacherRepository,
    private val settingsRepository: SettingsRepository,
) {

    suspend operator fun invoke(): User? {
        val authUser = authRepository.getAuthUser() ?: return null

        return when (settingsRepository.getUserType()) {
            UserType.STUDENT -> studentRepository.getUserByEmail(authUser.email).getOrNull()
            UserType.TEACHER -> teacherRepository.getUserByEmail(authUser.email).getOrNull()
            else -> null
        }
    }
}
