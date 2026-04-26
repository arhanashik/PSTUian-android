package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository

class GetTeacherProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
    private val teacherRepository: TeacherRepository,
    private val userPresenceRepository: UserPresenceRepository,
) {

    suspend operator fun invoke(userId: Int): DomainResult<UserProfile.TeacherProfile> {
        val authUser = authRepository.getAuthUser()

        val teacher = teacherRepository.getUser(userId).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(teacher.facultyId).getOrElse { return DomainResult.failure(it) }
        val isSignedIn = teacher.email == authUser?.email
        val isOnline = if (teacher.userId == authUser?.userId) {
            true
        } else {
            userPresenceRepository.isUserOnline(teacher.userId)
        }

        return DomainResult.success(UserProfile.TeacherProfile(teacher, faculty, isSignedIn, isOnline))
    }
}
