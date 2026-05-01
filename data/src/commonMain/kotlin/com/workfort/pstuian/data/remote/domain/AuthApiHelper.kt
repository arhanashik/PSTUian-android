package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto

interface AuthApiHelper {

    suspend fun signInStudent(
        email: String,
        deviceId: String
    ): NetworkResult<StudentDto>

    suspend fun signInTeacher(
        email: String,
        deviceId: String
    ): NetworkResult<TeacherDto>

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

    suspend fun updateAuthUserId(
        authUserId: String,
        userType: String,
        email: String,
        password: String,
    ): NetworkResult<String>

    suspend fun signOut(
        authUserId: String,
        userType: String,
        deviceId: String,
        fromAllDevice: Boolean = false,
    ): NetworkResult<Unit>

    suspend fun changePassword(
        email: String,
        userType: String,
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