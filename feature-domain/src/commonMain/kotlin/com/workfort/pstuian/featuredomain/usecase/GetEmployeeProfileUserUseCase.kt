package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository

class GetEmployeeProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
    private val userPresenceRepository: UserPresenceRepository,
) {

    suspend operator fun invoke(userId: Int): DomainResult<UserProfile.EmployeeProfile> {
        val authUser = authRepository.getAuthUser()

        val employee = facultyRepository.getEmployee(userId).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(employee.facultyId).getOrElse { return DomainResult.failure(it) }
        val isSignedIn = employee.email == authUser?.email
        val isOnline = if (employee.userId == authUser?.userId) {
            true
        } else {
            userPresenceRepository.isUserOnline(employee.userId)
        }

        return DomainResult.success(UserProfile.EmployeeProfile(employee, faculty, isSignedIn, isOnline))
    }
}
