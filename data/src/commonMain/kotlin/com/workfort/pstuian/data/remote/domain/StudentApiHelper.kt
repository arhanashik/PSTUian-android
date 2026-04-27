package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto

interface StudentApiHelper {

    suspend fun get(studentId: Int): NetworkResult<StudentDto>

    suspend fun getByEmail(email: String): NetworkResult<StudentDto>

    suspend fun changeProfileImage(userId: String, imageUrl: String): NetworkResult<Unit>

    suspend fun changeName(userId: String, name: String): NetworkResult<Unit>

    suspend fun changeBio(userId: String, bio: String): NetworkResult<Unit>

    suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        studentOldId: Int,
        studentId: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int
    ): NetworkResult<StudentDto>

    suspend fun changeConnectInfo(
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String
    ): NetworkResult<StudentDto>
}