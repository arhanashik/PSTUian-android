package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.service.AuthApiService


class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {

    override suspend fun signInStudent(
        userId: String,
        email: String,
        password: String,
        deviceId: String
    ): NetworkResult<Pair<StudentDto, String?>> {
        return runCatching {
            val response = service.signInStudent(userId, email, password, deviceId)
            if (response.isError || response.data == null) {
                NetworkResult.failure(NetworkError(response.responseCode))
            } else {
                NetworkResult.Success(response.data to response.authToken)
            }
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun signInTeacher(
        userId: String,
        email: String,
        password: String,
        deviceId: String
    ): NetworkResult<Pair<TeacherDto, String?>> {
        return runCatching {
            val response = service.signInTeacher(userId, email, password, deviceId)
            if (response.isError || response.data == null) {
                NetworkResult.failure(NetworkError(response.responseCode))
            } else {
                NetworkResult.Success(response.data to response.authToken)
            }
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
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
        password: String,
        deviceId: String,
    ): NetworkResult<StudentDto> {
        return runCatching {
            service.signUpStudent(
                name,
                id,
                reg,
                facultyId,
                batchId,
                session,
                email,
                password,
                deviceId,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        password: String,
        deviceId: String,
    ): NetworkResult<TeacherDto> {
        return runCatching {
            service.signUpTeacher(
                name,
                facultyId,
                designation,
                department,
                email,
                password,
                deviceId,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun updateUserId(
        userId: String,
        userType: String,
        email: String,
        password: String
    ): NetworkResult<String> {
        return runCatching {
            service.updateUserId(userId, userType, email, password).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun signOut(
        userId: String,
        userType: String,
        deviceId: String,
        fromAllDevice: Boolean,
    ): NetworkResult<Unit> {
        return runCatching {
            if (fromAllDevice) {
                service.signOutFromAllDevice(userId, userType, deviceId).toNetworkResult()
            } else {
                service.signOut(userId, userType, deviceId).toNetworkResult()
            }
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun changePassword(
        userType: String,
        email: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String,
    ): NetworkResult<String> {
        return runCatching {
            service.changePassword(userType, email, oldPassword, newPassword, deviceId).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun deleteAccount(
        email: String,
        userType: String,
        password: String,
    ): NetworkResult<Unit> {
        return runCatching {
            service.deleteAccount(email, userType, password).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}