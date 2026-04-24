package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.TeacherApiHelper
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.repository.TeacherRepository

class TeacherRepositoryImpl(
    private val helper: TeacherApiHelper,
    private val domainErrorMapper: DomainErrorMapper,
) : TeacherRepository {
    private val cache = mutableSetOf<User.Teacher>()

    override suspend fun getUser(userId: Int): DomainResult<User.Teacher> {
        return helper.get(userId).toDomainResult(domainErrorMapper).map { it.toModel() }
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
        teacher: User.Teacher,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): User.Teacher {
        helper.changeAcademicInfo(
            teacher.userId.toInt(), name, designation, department, blood, facultyId
        ).toModel().let { updated ->
            cache.add(updated)
            return updated
        }
    }

    override suspend fun changeConnectInfo(
        teacher: User.Teacher,
        address: String,
        phone: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): User.Teacher {
        val oldEmail = teacher.email
        helper.changeConnectInfo(
            teacher.userId.toInt(), address, phone, email, oldEmail, linkedIn, fbLink
        ).toModel().let { updated ->
            cache.add(updated)
            return updated
        }
    }
}
