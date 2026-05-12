package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.TeacherRepository

class TeacherRepositoryImpl(
    private val helper: TeacherApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : TeacherRepository {
    private val cache = mutableSetOf<User.Teacher>()

    override suspend fun getUser(id: Int): DomainResult<User.Teacher> {
        cache.firstOrNull { it.userId == id }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.get(id)
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { cache.add(it) }
    }

    override suspend fun getUserByEmail(email: String): DomainResult<User.Teacher> {
        cache.firstOrNull { it.email == email }?.let { cache ->
            return DomainResult.success(cache)
        }
        return helper.getByEmail(email)
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { cache.add(it) }
    }

    override suspend fun changeProfileImage(userId: Int, imageUrl: String): DomainResult<Unit> {
        return helper.changeProfileImage(imageUrl)
            .toDomainResult(domainErrorMapper)
            .onSuccess { cache.removeAll { it.userId == userId } }
    }

    override suspend fun changeName(userId: Int, name: String): DomainResult<Unit> {
        return helper.changeName(name).toDomainResult(domainErrorMapper).onSuccess {
            cache.removeAll { it.userId == userId }
        }
    }

    override suspend fun changeBio(userId: Int,  bio: String): DomainResult<Unit> {
        return helper.changeBio(bio).toDomainResult(domainErrorMapper).onSuccess {
            cache.removeAll { it.userId == userId }
        }
    }

    override suspend fun changeAcademicInfo(
        userId: Int,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): DomainResult<User.Teacher> {
        return helper.changeAcademicInfo(
            name,
            designation,
            department,
            blood,
            facultyId,
        )
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { updatedData ->
                cache.removeAll { it.userId == userId }
                cache.add(updatedData)
            }
    }

    override suspend fun changeConnectInfo(
        userId: Int,
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): DomainResult<User.Teacher> {
        return helper.changeConnectInfo(
            address,
            phone,
            oldEmail,
            email,
            linkedIn,
            fbLink,
        )
            .toDomainResult(domainErrorMapper)
            .map { it.toModel() }
            .onSuccess { updatedData ->
                cache.removeAll { it.userId == userId }
                cache.add(updatedData)
            }
    }
}
