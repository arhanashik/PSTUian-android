package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.TeacherDto

interface TeacherApiHelper {
    suspend fun get(id: Int): NetworkResult<TeacherDto>

    suspend fun getByEmail(email: String): NetworkResult<TeacherDto>

    suspend fun changeProfileImage(
        id: Int,
        imageUrl: String,
    ): Boolean

    suspend fun changeName(id: Int, name: String): Boolean

    suspend fun changeBio(id: Int, bio: String): Boolean

    suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int
    ): NetworkResult<TeacherDto>

    suspend fun changeConnectInfo(
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String
    ): NetworkResult<TeacherDto>
}