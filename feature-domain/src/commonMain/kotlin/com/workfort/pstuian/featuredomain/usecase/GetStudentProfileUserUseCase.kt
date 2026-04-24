package com.workfort.pstuian.featuredomain.usecase

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class GetStudentProfileUserUseCase(
    private val authRepository: AuthRepository,
    private val facultyRepository: FacultyRepository,
    private val studentRepository: StudentRepository,
) {

    suspend operator fun invoke(studentId: Int): DomainResult<StudentProfile> {
        val authUserEmail = authRepository.getAuthUser()?.email

        val student = studentRepository.getUser(studentId).getOrElse { return DomainResult.failure(it) }
        val faculty = facultyRepository.getFaculty(student.facultyId).getOrElse { return DomainResult.failure(it) }
        val batch = facultyRepository.getBatch(student.batchId).getOrElse { return DomainResult.failure(it) }

        return DomainResult.success(
            StudentProfile(student, faculty, batch, isSignedIn = student.email == authUserEmail),
        )
    }
}
