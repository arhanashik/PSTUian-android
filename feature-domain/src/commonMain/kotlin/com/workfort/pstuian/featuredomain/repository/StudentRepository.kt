package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User

interface StudentRepository {

    suspend fun getUser(userId: Int): DomainResult<User.Student>

    suspend fun getUserByEmail(email: String): DomainResult<User.Student>

    suspend fun changeProfileImage(userId: Int, imageUrl: String): DomainResult<Unit>

    suspend fun changeName(authUserId: String, name: String): DomainResult<Unit>

    suspend fun changeBio(authUserId: String, bio: String): DomainResult<Unit>

    suspend fun changeCvUrl(userId: Int, fileUrl: String): DomainResult<Unit>

    suspend fun changeAcademicInfo(
        authUserId: String,
        name: String,
        studentOldId: Int,
        studentId: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): DomainResult<User.Student>

    suspend fun changeConnectInfo(
        authUserId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        facebook: String,
    ): DomainResult<User.Student>
}