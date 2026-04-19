package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.StudentProfile
import com.workfort.pstuian.featuredomain.model.User

interface StudentRepository {

    suspend fun getUser(userId: String): User.Student?

    suspend fun getProfile(studentId: String): StudentProfile?

    suspend fun changeProfileImage(userId: String, imageUrl: String): DomainResult<Unit>

    suspend fun changeName(userId: String, name: String): DomainResult<Unit>

    suspend fun changeBio(userId: String, bio: String): DomainResult<Unit>

    suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        studentId: String,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): DomainResult<Unit>

    suspend fun changeConnectInfo(
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        facebook: String,
    ): DomainResult<Unit>
}