package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.mapper.toNetworkResult
import com.workfort.pstuian.data.model.ApiResponseCode
import com.workfort.pstuian.data.model.CommonNetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.service.AuthApiService


class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {

    override suspend fun validateStudentSignIn(deviceId: String): NetworkResult<StudentDto> {
        return runCatching {
            service.validateStudentSignIn(deviceId).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun validateTeacherSignIn(deviceId: String): NetworkResult<TeacherDto> {
        return runCatching {
            service.validateTeacherSignIn(deviceId).toNetworkResult()
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
        deviceId: String,
    ): NetworkResult<Unit> {
        return runCatching {
            service.signUpTeacher(
                name,
                facultyId,
                designation,
                department,
                deviceId,
            ).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun activateAccount(userType: String): NetworkResult<Unit> {
        return runCatching {
            service.activateAccount(userType).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun deactivateAccount(userType: String): NetworkResult<Unit> {
        return runCatching {
            service.deactivateAccount(userType).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }

    override suspend fun signOut(userType: String, clearAllSession: Boolean): NetworkResult<Unit> {
        return runCatching {
            service.signOut(userType, clearAllSession).toNetworkResult()
        }.getOrElse {
            NetworkResult.failure(CommonNetworkError.apiError(ApiResponseCode.Unknown))
        }
    }
}