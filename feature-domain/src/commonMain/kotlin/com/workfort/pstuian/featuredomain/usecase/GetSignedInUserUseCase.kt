package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class GetSignedInUserUseCase(
    private val authRepository: AuthRepository,
    private val studentRepository: StudentRepository,
    private val sharedPrefRepository: SharedPrefRepository,
) {
    suspend operator fun invoke(): User? {
        val authUser = authRepository.getAuthUser() ?: return null

        return when (authRepository.getSignInUserType()) {
            UserType.STUDENT -> studentRepository.getUser(authUser.userId)
            UserType.TEACHER -> studentRepository.getUser(authUser.userId) // TODO: get teacher
            UserType.EMPLOYEE -> studentRepository.getUser(authUser.userId) // TODO: get employee
            else -> null
        }
    }
}
