package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class GetStudentProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
    private val studentRepository: StudentRepository,
) {

    suspend operator fun invoke(studentId: Int): DomainResult<UserProfile.StudentProfile> {
        val authUser = authRepository.getAuthUser()

        val user = studentRepository.getUser(studentId).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(user.facultyId).getOrElse { return DomainResult.failure(it) }
        val batch = facultyRepository.getBatch(user.batchId).getOrElse { return DomainResult.failure(it) }
        val isSignedIn = user.email == authUser?.email

        return DomainResult.success(UserProfile.StudentProfile(user, faculty, isSignedIn, batch))
    }
}
