package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User

interface TeacherRepository {

    suspend fun getUser(userId: Int): DomainResult<User.Teacher>

    suspend fun getUserByEmail(email: String): DomainResult<User.Teacher>

    suspend fun changeProfileImage(teacher: User.Teacher, imageUrl: String): Boolean

    suspend fun changeName(teacher: User.Teacher, name: String): Boolean

    suspend fun changeBio(teacher: User.Teacher, bio: String): Boolean

    suspend fun changeAcademicInfo(
        teacher: User.Teacher,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): User.Teacher

    suspend fun changeConnectInfo(
        teacher: User.Teacher,
        address: String,
        phone: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): User.Teacher
}