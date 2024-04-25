package com.workfort.pstuian.networking.domain

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.ConfigEntity
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity

interface AuthApiHelper {
    suspend fun getConfig(): ConfigEntity

    suspend fun getAllDevices(
        userId: Int,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<DeviceEntity>

    suspend fun registerDevice(device: DeviceEntity): DeviceEntity

    suspend fun updateFcmToken(
        deviceId: String,
        fcmToken: String
    ): DeviceEntity

    suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String
    ): Pair<StudentEntity, String>

    suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String
    ): Pair<TeacherEntity, String>

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        deviceId: String
    ): Pair<StudentEntity, String>

    suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
        deviceId: String,
    ): Pair<TeacherEntity, String>

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