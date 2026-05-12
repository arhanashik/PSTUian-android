package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.featuredomain.model.DomainResult

interface TeacherApiHelper {
    suspend fun get(id: Int): NetworkResult<TeacherDto>

    suspend fun getByEmail(email: String): NetworkResult<TeacherDto>

    suspend fun changeProfileImage(imageUrl: String): NetworkResult<Unit>

    suspend fun changeName(name: String): NetworkResult<Unit>

    suspend fun changeBio(bio: String): NetworkResult<Unit>

    suspend fun changeAcademicInfo(
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int,
    ): NetworkResult<TeacherDto>

    suspend fun changeConnectInfo(
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String,
    ): NetworkResult<TeacherDto>
}