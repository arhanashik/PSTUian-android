package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.UserProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.TeacherRepository

class GetTeacherProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
    private val teacherRepository: TeacherRepository,
) {

    suspend operator fun invoke(id: Int): DomainResult<UserProfile.TeacherProfile> {
        val authUser = authRepository.getAuthUser()

        val teacher = teacherRepository.getUser(id).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(teacher.facultyId).getOrElse { return DomainResult.failure(it) }
        val isSignedIn = teacher.authUserId == authUser?.userId

        return DomainResult.success(UserProfile.TeacherProfile(teacher, faculty, isSignedIn))
    }
}
