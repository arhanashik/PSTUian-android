package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.device.DeviceEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity

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

    suspend fun registerDevice(device: DeviceEntity): DeviceEntity

    suspend fun updateFcmToken(
        deviceId: String,
        fcmToken: String
    ): DeviceEntity

    suspend fun signIn(
        email: String,
        password: String,
        userType: String,
        deviceId: String
    ): Pair<StudentEntity, String>

    suspend fun signUp(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        deviceId: String
    ): Pair<StudentEntity, String>

    suspend fun signOut(
        id: Int,
        userType: String
    ): String
}