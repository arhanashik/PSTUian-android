package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.device.DeviceEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity

/**
 *  ****************************************************************************
 *  * Created by : arhan on 02 Oct, 2021 at 5:10 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/2/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface AuthApiHelper {
    suspend fun getConfig(): ConfigEntity

    suspend fun getAllDevices(
        userId: Int,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int = Const.Params.Default.PAGE_SIZE
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
}