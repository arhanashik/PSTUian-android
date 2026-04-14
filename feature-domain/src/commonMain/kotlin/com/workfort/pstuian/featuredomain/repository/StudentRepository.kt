package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.StudentProfile

interface StudentRepository {
    suspend fun getProfile(studentId: Int): StudentProfile

    suspend fun changeProfileImage(student: StudentEntity, imageUrl: String): Boolean

    suspend fun changeName(student: StudentEntity, name: String): Boolean

    suspend fun changeBio(student: StudentEntity, bio: String): Boolean

    suspend fun changeAcademicInfo(
        student: StudentEntity,
        name: String,
        id: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): StudentEntity

    suspend fun changeConnectInfo(
        student: StudentEntity,
        address: String,
        phone: String,
        email: String,
        cvLink: String,
        linkedIn: String,
        facebook: String,
    ): StudentEntity
}