package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.FacultyRepository
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class StudentRepositoryImpl(
    private val authRepo: AuthRepository,
    private val facultyRepo: FacultyRepository,
    private val helper: StudentApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : StudentRepository {
    private val students = mutableMapOf<String, User.Student>()

    override suspend fun getUser(userId: String): User.Student? {
        return helper.get(userId)?.toModel()
    }

    override suspend fun getUserByEmail(email: String): DomainResult<User.Student> {
        return helper.getByEmail(email).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun getProfile(studentId: String): StudentProfile? {
        // get student
        val student = students[studentId] ?: helper.get(studentId)?.toModel()
        if (student == null) {
            return null
        }
        // get faculty
        val faculty = facultyRepo.getFaculty(student.facultyId)
        // get batch
        val batch = facultyRepo.getBatch(student.batchId)
        // get sign in state
        val isSignedIn = authRepo.getAuthUser()?.userId == studentId

        return StudentProfile(student, faculty, batch, isSignedIn)
    }

    override suspend fun changeProfileImage(
        userId: String,
        imageUrl: String
    ): DomainResult<Unit> {
        return helper.changeProfileImage(userId, imageUrl).toDomainResult(domainErrorMapper)
    }

    override suspend fun changeName(
        userId: String,
        name: String
    ): DomainResult<Unit> {
        return helper.changeName(userId, name).toDomainResult(domainErrorMapper)
    }

    override suspend fun changeBio(
        userId: String,
        bio: String
    ): DomainResult<Unit> {
        return helper.changeBio(userId, bio).toDomainResult(domainErrorMapper)
    }

    override suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        studentId: String,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): DomainResult<Unit> {
        return helper.changeAcademicInfo(
            userId = userId,
            name = name,
            studentId = studentId,
            reg = reg,
            blood = blood,
            facultyId = facultyId,
            session = session,
            batchId = batchId,
        ).toDomainResult(domainErrorMapper)
    }

    override suspend fun changeConnectInfo(
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        facebook: String
    ): DomainResult<Unit> {
        return helper.changeConnectInfo(
            userId = userId,
            address = address,
            phone = phone,
            oldEmail = oldEmail,
            newEmail = newEmail,
            cvLink = cvLink,
            linkedIn = linkedIn,
            fbLink = facebook,
        ).toDomainResult(domainErrorMapper)
    }
}
