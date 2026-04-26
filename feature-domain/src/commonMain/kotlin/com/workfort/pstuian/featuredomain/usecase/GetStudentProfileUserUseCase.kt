package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository

class GetStudentProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
    private val studentRepository: StudentRepository,
    private val userPresenceRepository: UserPresenceRepository,
) {

    suspend operator fun invoke(studentId: Int): DomainResult<UserProfile.StudentProfile> {
        val authUser = authRepository.getAuthUser()

        val student = studentRepository.getUser(studentId).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(student.facultyId).getOrElse { return DomainResult.failure(it) }
        val batch = facultyRepository.getBatch(student.batchId).getOrElse { return DomainResult.failure(it) }
        val isSignedIn = student.email == authUser?.email
        val isOnline = if (student.userId == authUser?.userId) {
            true
        } else {
            userPresenceRepository.isUserOnline(student.userId)
        }

        return DomainResult.success(
            UserProfile.StudentProfile(student, faculty, batch, isSignedIn, isOnline),
        )
    }
}
