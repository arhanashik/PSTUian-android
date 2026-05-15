package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.StudentApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.StudentRepository

class StudentRepositoryImpl(
    private val helper: StudentApiHelper,
) : StudentRepository {

    private val cache = mutableSetOf<User.Student>()

    override suspend fun getUser(userId: Int): DomainResult<User.Student> {
        cache.firstOrNull { it.userId == userId }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.get(userId)
            .toDomainResult()
            .map { it.toModel() }
            .onSuccess { cache.add(it) }
    }

    override suspend fun getUserByEmail(email: String): DomainResult<User.Student> {
        cache.firstOrNull { it.email == email }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.getByEmail(email)
            .toDomainResult()
            .map { it.toModel() }
            .onSuccess { cache.add(it) }
    }

    override suspend fun changeProfileImage(userId: Int, imageUrl: String): DomainResult<Unit> {
        return helper.changeProfileImage(imageUrl)
            .toDomainResult()
            .onSuccess { cache.removeAll { it.userId == userId } }
    }

    override suspend fun changeName(
        userId: Int,
        name: String
    ): DomainResult<Unit> {
        return helper.changeName(name).toDomainResult().onSuccess {
            cache.removeAll { it.userId == userId }
        }
    }

    override suspend fun changeBio(
        userId: Int,
        bio: String
    ): DomainResult<Unit> {
        return helper.changeBio(bio).toDomainResult().onSuccess {
            cache.removeAll { it.userId == userId }
        }
    }

    override suspend fun changeCvUrl(
        userId: Int,
        fileUrl: String
    ): DomainResult<Unit> {
        return helper.changeCvUrl(fileUrl)
            .toDomainResult()
            .onSuccess { cache.removeAll { it.userId == userId } }
    }

    override suspend fun changeAcademicInfo(
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
            name = name,
            studentOldId = studentOldId,
            studentId = studentId,
            reg = reg,
            blood = blood,
            facultyId = facultyId,
            session = session,
            batchId = batchId,
        )
            .toDomainResult()
            .map { it.toModel() }
            .onSuccess { student ->
                cache.removeAll { it.userId == studentOldId }
                cache.add(student)
            }
    }

    override suspend fun changeConnectInfo(
        userId: Int,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        facebook: String
    ): DomainResult<User.Student> {
        return helper.changeConnectInfo(
            address = address,
            phone = phone,
            oldEmail = oldEmail,
            newEmail = newEmail,
            cvLink = cvLink,
            linkedIn = linkedIn,
            fbLink = facebook,
        )
            .toDomainResult()
            .map { it.toModel() }
            .onSuccess { student ->
                cache.removeAll { it.userId == userId }
                cache.add(student)
            }
    }
}
