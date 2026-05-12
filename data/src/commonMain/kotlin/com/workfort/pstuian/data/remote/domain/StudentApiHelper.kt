package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto

interface StudentApiHelper {

    suspend fun get(studentId: Int): NetworkResult<StudentDto>

    suspend fun getByEmail(email: String): NetworkResult<StudentDto>

    suspend fun changeProfileImage(imageUrl: String): NetworkResult<Unit>

    suspend fun changeName(name: String): NetworkResult<Unit>

    suspend fun changeBio(bio: String): NetworkResult<Unit>

    suspend fun changeCvUrl(fileUrl: String): NetworkResult<Unit>

    suspend fun changeAcademicInfo(
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
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String
    ): NetworkResult<StudentDto>
}