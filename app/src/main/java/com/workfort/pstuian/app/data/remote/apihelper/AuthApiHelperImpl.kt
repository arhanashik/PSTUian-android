package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.device.DeviceEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.util.remote.AuthApiService

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 3:52 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

class AuthApiHelperImpl(private val service: AuthApiService) : AuthApiHelper {
    override suspend fun getConfig(): ConfigEntity {
        val response = service.getConfig()
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun registerDevice(device: DeviceEntity): DeviceEntity {
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

    override suspend fun updateFcmToken(deviceId: String, fcmToken: String): DeviceEntity {
        val response = service.updateFcmToken(deviceId, fcmToken)
        if(!response.success) throw Exception(response.message)
        return response.data?: throw Exception("No data found")
    }

    override suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String
    ): Pair<StudentEntity, String> {
        val response = service.signInStudent(email, password, deviceId)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String
    ): Pair<TeacherEntity, String> {
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
        deviceId: String
    ): Pair<StudentEntity, String> {
        val response = service.signUpStudent(name, id, reg, facultyId, batchId,
            session, email, deviceId)
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
    ): Pair<TeacherEntity, String> {
        val response = service.signUpTeacher(name, designation, department, email,
            password, facultyId, deviceId)
        if(!response.success) throw Exception(response.message)
        return Pair(response.data!!, response.authToken!!)
    }

    override suspend fun signOut(id: Int, userType: String): String {
        val response = service.signOut(id, userType)
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

    override suspend fun forgotPassword(userType: String, email: String): String {
        val response = service.forgotPassword(userType, email)
        if(!response.success) throw Exception(response.message)
        return response.message
    }

    override suspend fun emailVerification(userType: String, email: String): String {
        val response = service.emailVerification(userType, email)
        if(!response.success) throw Exception(response.message)
        return response.message
    }
}