package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.model.TeacherProfile

interface TeacherRepository {
    suspend fun getProfile(teacherId: Int): TeacherProfile

    suspend fun changeProfileImage(teacher: TeacherEntity, imageUrl: String): Boolean

    suspend fun changeName(teacher: TeacherEntity, name: String): Boolean

    suspend fun changeBio(teacher: TeacherEntity, bio: String): Boolean

    suspend fun changeAcademicInfo(
        teacher: TeacherEntity,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): TeacherEntity

    suspend fun changeConnectInfo(
        teacher: TeacherEntity,
        address: String,
        phone: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): TeacherEntity
}