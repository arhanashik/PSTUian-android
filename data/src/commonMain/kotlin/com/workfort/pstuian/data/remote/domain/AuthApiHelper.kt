package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult

interface AuthApiHelper {

    suspend fun validateSignIn(userType: String, email: String, deviceId: String): NetworkResult<Unit>

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        deviceId: String,
    ): NetworkResult<Unit>

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        deviceId: String,
    ): NetworkResult<Unit>

    suspend fun updateAuthUserId(userType: String): NetworkResult<Unit>

    suspend fun deleteAccount(userType: String): NetworkResult<Unit>
}