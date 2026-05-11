package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User

interface TeacherRepository {

    suspend fun getUser(id: Int): DomainResult<User.Teacher>

    suspend fun getUserByEmail(email: String): DomainResult<User.Teacher>

    suspend fun changeProfileImage(userId: Int, imageUrl: String): DomainResult<Unit>

    suspend fun changeName(teacher: User.Teacher, name: String): Boolean

    suspend fun changeBio(teacher: User.Teacher, bio: String): Boolean

    suspend fun changeAcademicInfo(
        authUserId: String,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): DomainResult<User.Teacher>

    suspend fun changeConnectInfo(
        authUserId: String,
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): DomainResult<User.Teacher>
}