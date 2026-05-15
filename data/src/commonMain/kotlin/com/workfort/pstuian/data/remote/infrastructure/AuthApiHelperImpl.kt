package com.workfort.pstuian.data.remote.infrastructure

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.safeApiCall
import com.workfort.pstuian.data.remote.service.AuthApiService

class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {

    override suspend fun validateStudentSignIn(deviceId: String): NetworkResult<StudentDto> {
        return safeApiCall { service.validateStudentSignIn(deviceId) }
    }

    override suspend fun validateTeacherSignIn(deviceId: String): NetworkResult<TeacherDto> {
        return safeApiCall { service.validateTeacherSignIn(deviceId) }
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
        return safeApiCall {
            service.signUpStudent(name, id, reg, facultyId, batchId, session, deviceId)
        }
    }

    override suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        deviceId: String,
    ): NetworkResult<Unit> {
        return safeApiCall {
            service.signUpTeacher(name, facultyId, designation, department, deviceId)
        }
    }

    override suspend fun activateAccount(userType: String): NetworkResult<Unit> {
        return safeApiCall { service.activateAccount(userType) }
    }

    override suspend fun deactivateAccount(userType: String): NetworkResult<Unit> {
        return safeApiCall { service.deactivateAccount(userType) }
    }

    override suspend fun signOut(userType: String, clearAllSession: Boolean): NetworkResult<Unit> {
        return safeApiCall { service.signOut(userType, clearAllSession) }
    }
}
