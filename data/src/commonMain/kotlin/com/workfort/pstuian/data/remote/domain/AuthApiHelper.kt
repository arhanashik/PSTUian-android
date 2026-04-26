package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.ConfigDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto

interface AuthApiHelper {

    suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String
    ): NetworkResult<Pair<StudentDto, String?>> // Dto and Auth Token

    suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String
    ): NetworkResult<Pair<TeacherDto, String?>> // Dto and Auth Token

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        password: String,
        deviceId: String,
    ): NetworkResult<StudentDto>

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        password: String,
        deviceId: String,
    ): NetworkResult<TeacherDto>

    suspend fun updateUserId(
        userId: String,
        userType: String,
        email: String,
        password: String,
    ): NetworkResult<String>

    suspend fun signOut(
        userId: String,
        userType: String,
        deviceId: String,
        fromAllDevice: Boolean = false,
    ): NetworkResult<Unit>

    suspend fun changePassword(
        userType: String,
        email: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String,
    ): NetworkResult<String>

    suspend fun deleteAccount(
        email: String,
        userType: String,
        password: String,
    ): NetworkResult<Unit>
}