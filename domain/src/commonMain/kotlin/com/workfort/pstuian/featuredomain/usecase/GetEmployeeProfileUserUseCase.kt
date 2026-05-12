package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository

class GetEmployeeProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
) {

    suspend operator fun invoke(userId: Int): DomainResult<UserProfile.EmployeeProfile> {
        val authUser = authRepository.getAuthUser()

        val user = facultyRepository.getEmployee(userId).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(user.facultyId).getOrElse { return DomainResult.failure(it) }
        val isSignedIn = user.email == authUser?.email

        return DomainResult.success(UserProfile.EmployeeProfile(user, faculty, isSignedIn))
    }
}
