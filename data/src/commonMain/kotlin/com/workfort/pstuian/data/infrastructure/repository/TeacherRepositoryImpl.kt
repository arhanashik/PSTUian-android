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
        return helper.get(id).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun getUserByEmail(email: String): DomainResult<User.Teacher> {
        return helper.getByEmail(email).toDomainResult(domainErrorMapper).map { it.toModel() }
    }

    override suspend fun changeProfileImage(teacher: User.Teacher, imageUrl: String): Boolean {
        val id = teacher.userId.toInt()
        val isChanged = helper.changeProfileImage(id, imageUrl)
        if (isChanged) {
            val updated = teacher.copy(imageUrl = imageUrl)
            cache.add(updated)
        }
        return isChanged
    }

    override suspend fun changeName(teacher: User.Teacher, name: String): Boolean {
        val id = teacher.userId.toInt()
        val isChanged = helper.changeName(id, name)
        if (isChanged) {
            val updated = teacher.copy(name = name)
            cache.add(updated)
        }
        return isChanged
    }

    override suspend fun changeBio(teacher: User.Teacher, bio: String): Boolean {
        val id = teacher.userId.toInt()
        val isChanged = helper.changeBio(id, bio)
        if (isChanged) {
            val updated = teacher.copy(bio = bio)
            cache.add(updated)
        }
        return isChanged
    }

    override suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): DomainResult<User.Teacher> {
        return helper.changeAcademicInfo(
            userId,
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
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): DomainResult<User.Teacher> {
        return helper.changeConnectInfo(
            userId,
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
