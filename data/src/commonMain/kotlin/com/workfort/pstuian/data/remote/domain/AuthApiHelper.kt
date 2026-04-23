package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.ConfigDto
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto

interface AuthApiHelper {
    suspend fun getConfig(): ConfigDto

    suspend fun signInStudent(
        userId: String,
        email: String,
        deviceId: String
    ): NetworkResult<Pair<StudentDto, String?>> // Dto and Auth Token

    suspend fun signInTeacher(
        userId: String,
        email: String,
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
        deviceId: String,
        password: String,
    ): Pair<StudentDto, String>

    suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
        deviceId: String,
    ): Pair<TeacherDto, String>

    suspend fun signOut(
        userId: String,
        userType: String,
        deviceId: String,
        fromAllDevice: Boolean = false,
    ): String

    /**
     * Inside the response pair,
     * 1st value should be message and
     * 2nd one should be auth token
     * */
    suspend fun changePassword(
        userId: String,
        userType: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String
    ): Pair<String, String?>

    suspend fun forgotPassword(userType: String, email: String, deviceId: String): String

    suspend fun emailVerification(userType: String, email: String, deviceId: String): String

    suspend fun deleteAccount(
        userId: String,
        userType: String,
        email: String,
        password: String
    ): String
}