package com.workfort.pstuian.networking.infrastructure

import com.workfort.pstuian.networking.service.AuthApiService
import com.workfort.pstuian.model.dto.ConfigDto
import com.workfort.pstuian.model.dto.DeviceDto
import com.workfort.pstuian.model.dto.StudentDto
import com.workfort.pstuian.model.dto.TeacherDto
import com.workfort.pstuian.networking.domain.AuthApiHelper



class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {
    override suspend fun getConfig(): ConfigDto {
        val response = service.getConfig()
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun getAllDevices(
        userId: Int,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int
    ): List<DeviceDto> {
        val response = service.getAllDevices(userId, userType, deviceId, page, limit)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun registerDevice(device: DeviceDto): DeviceDto {
        val response = service.registerDevice(
            id = device.id,
            fcmToken = device.fcmToken,
            model = device.model?: "",
            androidVersion = device.androidVersion?: "",
            appVersionCode = device.appVersionCode?: 0,
            appVersionName = device.appVersionName?: "",
            ipAddress = device.ipAddress?: "",
            lat = device.lat?: "0.0",
            lng = device.lng?: "0.0",
            locale = device.locale?: ""
        )
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun updateFcmToken(deviceId: String, fcmToken: String): DeviceDto {
        val response = service.updateFcmToken(deviceId, fcmToken)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String
    ): Pair<StudentDto, String> {
        val response = service.signInStudent(email, password, deviceId)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String
    ): Pair<TeacherDto, String> {
        val response = service.signInTeacher(email, password, deviceId)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
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
        id: Int,
        userType: String,
        deviceId: String,
        fromAllDevice: Boolean,
    ): String {
        val response = if(fromAllDevice) service.signOutFromAllDevice(
            id, userType, deviceId
        ) else service.signOut(id, userType, deviceId)

        if(!response.success) throw Exception(response.message)
        return response.message
    }

    override suspend fun changePassword(
        userId: Int,
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
        userId: Int,
        userType: String,
        email: String,
        password: String
    ): String {
        val response = service.deleteAccount(userId, userType, email, password)
        if(!response.success) throw Exception(response.message)
        return response.message
    }
}