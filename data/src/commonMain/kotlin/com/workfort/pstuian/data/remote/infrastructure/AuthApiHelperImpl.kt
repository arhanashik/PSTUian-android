package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.ConfigDto
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.service.AuthApiService


class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {

    override suspend fun getConfig(): ConfigDto {
        val response = service.getConfig()
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun signInStudent(
        userId: String,
        email: String,
        deviceId: String
    ): NetworkResult<Pair<StudentDto, String?>> {
        val response = service.signInStudent(userId, email, deviceId)
        return if (response.isError || response.data == null) {
            NetworkResult.failure(NetworkError(response.responseCode))
        } else {
            NetworkResult.Success(response.data to response.authToken)
        }
    }

    override suspend fun signInTeacher(
        userId: String,
        email: String,
        deviceId: String
    ): NetworkResult<Pair<TeacherDto, String?>> {
        val response = service.signInTeacher(userId, email, deviceId)
        return if (response.isError || response.data == null) {
            NetworkResult.failure(NetworkError(response.responseCode))
        } else {
            NetworkResult.Success(response.data to response.authToken)
        }
    }

    override suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        deviceId: String,
        password: String,
    ): Pair<StudentDto, String> {
        val response = service.signUpStudent(
            name,
            id,
            reg,
            facultyId,
            batchId,
            session,
            email,
            deviceId,
            password,
        )
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
        deviceId: String
    ): Pair<TeacherDto, String> {
        val response = service.signUpTeacher(name, designation, department, email,
            password, facultyId, deviceId)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signOut(
        userId: String,
        userType: String,
        deviceId: String,
        fromAllDevice: Boolean,
    ): String {
        val response = if(fromAllDevice) service.signOutFromAllDevice(
            userId, userType, deviceId
        ) else service.signOut(userId, userType, deviceId)

        if(!response.success) throw Exception(response.message)
        return response.message
    }

    override suspend fun changePassword(
        userId: String,
        userType: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String
    ): Pair<String, String?> {
        val response = service.changePassword(userId, userType, oldPassword, newPassword, deviceId)
        if(!response.success) throw Exception(response.message)
        return Pair(response.message, response.authToken)
    }

    override suspend fun forgotPassword(
        userType: String,
        email: String,
        deviceId: String
    ): String {
        val response = service.forgotPassword(userType, email, deviceId)
        if(!response.success) throw Exception(response.message)
        return response.message
    }

    override suspend fun emailVerification(
        userType: String,
        email: String,
        deviceId: String
    ): String {
        val response = service.emailVerification(userType, email, deviceId)
        if(!response.success) throw Exception(response.message)
        return response.message
    }

    override suspend fun deleteAccount(
        userId: String,
        userType: String,
        email: String,
        password: String
    ): String {
        val response = service.deleteAccount(userId, userType, email, password)
        if(!response.success) throw Exception(response.message)
        return response.message
    }
}