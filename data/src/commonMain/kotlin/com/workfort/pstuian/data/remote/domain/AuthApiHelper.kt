package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto

interface AuthApiHelper {

    suspend fun validateStudentSignIn(deviceId: String): NetworkResult<StudentDto>

    suspend fun validateTeacherSignIn(deviceId: String): NetworkResult<TeacherDto>

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        deviceId: String,
    ): NetworkResult<Unit>

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        deviceId: String,
    ): NetworkResult<Unit>

    suspend fun activateAccount(userType: String): NetworkResult<Unit>

    suspend fun deactivateAccount(userType: String): NetworkResult<Unit>

    suspend fun signOut(userType: String, clearAllSession: Boolean): NetworkResult<Unit>
}