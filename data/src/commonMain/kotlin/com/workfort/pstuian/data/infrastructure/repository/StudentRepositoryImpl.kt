package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class StudentRepositoryImpl(
    private val helper: StudentApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : StudentRepository {

    private val cache = mutableSetOf<User.Student>()

    override suspend fun getUser(studentId: Int): DomainResult<User.Student> {
        cache.firstOrNull { it.id == studentId }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.get(studentId)
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { cache.add(it) }
    }

    override suspend fun getUserByEmail(email: String): DomainResult<User.Student> {
        cache.firstOrNull { it.email == email }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.getByEmail(email)
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { cache.add(it) }
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
        studentOldId: Int,
        studentId: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): DomainResult<User.Student> {
        return helper.changeAcademicInfo(
            userId = userId,
            name = name,
            studentOldId = studentOldId,
            studentId = studentId,
            reg = reg,
            blood = blood,
            facultyId = facultyId,
            session = session,
            batchId = batchId,
        )
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { student ->
                cache.removeAll { it.userId == userId }
                cache.add(student)
            }
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
    ): DomainResult<User.Student> {
        return helper.changeConnectInfo(
            userId = userId,
            address = address,
            phone = phone,
            oldEmail = oldEmail,
            newEmail = newEmail,
            cvLink = cvLink,
            linkedIn = linkedIn,
            fbLink = facebook,
        )
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { student ->
                cache.removeAll { it.userId == userId }
                cache.add(student)
            }
    }
}
