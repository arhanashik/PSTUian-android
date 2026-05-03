package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.service.AuthApiService


class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {

    override suspend fun validateSignIn(userType: String, email: String, deviceId: String): NetworkResult<Unit> {
        return runCatching {
            service.validateSignIn(userType, email, deviceId).toNetworkResult()
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
        deviceId: String,
    ): NetworkResult<Unit> {
        return runCatching {
            service.signUpStudent(
                name,
                id,
                reg,
                facultyId,
                batchId,
                session,
                email,
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
        deviceId: String,
    ): NetworkResult<Unit> {
        return runCatching {
            service.signUpTeacher(
                name,
                facultyId,
                designation,
                department,
                email,
                deviceId,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun updateAuthUserId(userType: String): NetworkResult<Unit> {
        return runCatching {
            service.updateAuthUserId(userType).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun deleteAccount(userType: String): NetworkResult<Unit> {
        return runCatching {
            service.deleteAccount(userType).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}