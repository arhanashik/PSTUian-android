package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User

interface StudentRepository {

    suspend fun getUser(studentId: Int): DomainResult<User.Student>

    suspend fun getUserByEmail(email: String): DomainResult<User.Student>

    suspend fun changeProfileImage(userId: String, imageUrl: String): DomainResult<Unit>

    suspend fun changeName(userId: String, name: String): DomainResult<Unit>

    suspend fun changeBio(userId: String, bio: String): DomainResult<Unit>

    suspend fun changeAcademicInfo(
        userId: String,
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
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        facebook: String,
    ): DomainResult<User.Student>
}