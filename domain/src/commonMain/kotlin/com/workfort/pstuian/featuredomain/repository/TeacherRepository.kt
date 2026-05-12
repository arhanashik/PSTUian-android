package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User

interface TeacherRepository {

    suspend fun getUser(id: Int): DomainResult<User.Teacher>

    suspend fun getUserByEmail(email: String): DomainResult<User.Teacher>

    suspend fun changeProfileImage(userId: Int, imageUrl: String): DomainResult<Unit>

    suspend fun changeName(userId: Int, name: String): DomainResult<Unit>

    suspend fun changeBio(userId: Int, bio: String): DomainResult<Unit>

    suspend fun changeAcademicInfo(
        userId: Int,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): DomainResult<User.Teacher>

    suspend fun changeConnectInfo(
        userId: Int,
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): DomainResult<User.Teacher>
}