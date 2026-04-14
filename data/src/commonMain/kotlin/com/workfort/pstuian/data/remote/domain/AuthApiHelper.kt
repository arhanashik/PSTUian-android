package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.data.dto.ConfigDto
import com.workfort.pstuian.data.dto.DeviceDto
import com.workfort.pstuian.data.dto.StudentDto
import com.workfort.pstuian.data.dto.TeacherDto

interface AuthApiHelper {
    suspend fun getConfig(): ConfigDto

    suspend fun getAllDevices(
        userId: Int,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<DeviceDto>

    suspend fun registerDevice(device: DeviceDto): DeviceDto

    suspend fun updateFcmToken(
        deviceId: String,
        fcmToken: String
    ): DeviceDto

    suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String
    ): Pair<StudentDto, String>

    suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String
    ): Pair<TeacherDto, String>

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
        id: Int,
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
        userId: Int,
        userType: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String
    ): Pair<String, String?>

    suspend fun forgotPassword(userType: String, email: String, deviceId: String): String

    suspend fun emailVerification(userType: String, email: String, deviceId: String): String

    suspend fun deleteAccount(
        userId: Int,
        userType: String,
        email: String,
        password: String
    ): String
}