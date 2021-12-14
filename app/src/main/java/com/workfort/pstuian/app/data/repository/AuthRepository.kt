package com.workfort.pstuian.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.workfort.pstuian.PstuianApp
import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.config.ConfigService
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.device.DeviceEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
import com.workfort.pstuian.app.data.remote.apihelper.AuthApiHelper
import com.workfort.pstuian.util.helper.AndroidUtil
import com.workfort.pstuian.util.helper.GsonUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.*

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 4:03 PM.
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

class AuthRepository(
    private val dbService: ConfigService,
    private val helper: AuthApiHelper
) {

    private val Context.authStore: DataStore<Preferences> by preferencesDataStore(AUTH_PREFERENCES)
    companion object {
        private val USER = stringPreferencesKey("user")
        private val USER_TYPE = stringPreferencesKey("user_type")
        private val DEVICE = stringPreferencesKey("device")
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private const val AUTH_PREFERENCES = "auth_preferences"
    }

    suspend fun getConfig(): ConfigEntity {
        val oldConfig = dbService.getLatest()
        return try {
            var newConfig = helper.getConfig()
            if(oldConfig == null || oldConfig != newConfig) {
                dbService.insert(newConfig)
            } else {
                // pass local states(forceRefreshDone and forceUpdateDone) into the new state
                if(oldConfig == newConfig) newConfig = oldConfig
            }

            newConfig
        } catch (ex: Exception) {
            Timber.e(ex)
            if(oldConfig == null) throw ex
            oldConfig
        }
    }

    suspend fun registerDevice(
        fcmToken: String,
        lat: String = "0.0",
        lng: String = "0.0",
    ): DeviceEntity {
        val deviceId = if(Prefs.deviceId.isNullOrEmpty()) {
            val newId = UUID.randomUUID().toString()
            Prefs.deviceId = newId
            newId
        } else Prefs.deviceId
        val newDevice = DeviceEntity(
            id = deviceId!!,
            fcmToken = fcmToken,
            model = AndroidUtil.getDeviceName(),
            androidVersion = AndroidUtil.getDeviceVersionName,
            appVersionCode = AndroidUtil.getVersionCode,
            appVersionName = AndroidUtil.getVersionName,
            ipAddress = AndroidUtil.getLocalIpAddress(),
            lat = lat,
            lng = lng,
            locale = AndroidUtil.getLocaleLanguage()
        )

        // device is registered and updated
        if(newDevice == getRegisteredDevice()) {
            return newDevice
        }

        // device is not registered yet/device is not updated
        helper.registerDevice(newDevice).also { device ->
            storeRegisteredDevice(device)
            return device
        }
    }

    suspend fun updateFcmToken(
        fcmToken: String
    ): DeviceEntity {
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Device is not registered yet!")

        helper.updateFcmToken(deviceId, fcmToken).also { device ->
            Prefs.fcmToken = fcmToken
            storeRegisteredDevice(device)
            return device
        }
    }

    private suspend fun storeRegisteredDevice(device: DeviceEntity) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[DEVICE] = GsonUtil.toJson(device)
        }
    }

    private fun getRegisteredDevice(): DeviceEntity? {
        val context = PstuianApp.getBaseApplicationContext()
        val jsonStr = runBlocking {
            context.authStore.data.first()
        }[DEVICE]?: return null

        return GsonUtil.fromJson(jsonStr)
    }

//    fun getSignedInUserFlow(): Flow<StudentEntity> {
//        val context = PstuianApp.getBaseApplicationContext()
//        return context.authStore.data
//            .map { auth ->
//                val jsonStr = auth[USER]?: ""
//                GsonUtil.fromJson(jsonStr)
//            }
//    }

    fun getSignInUserType(): String {
        val context = PstuianApp.getBaseApplicationContext()
        return runBlocking { context.authStore.data.first() }[USER_TYPE]
            ?: throw Exception("Unknown user type")
    }

    fun getSignInUser(): Any {
        val context = PstuianApp.getBaseApplicationContext()
        val userType = getSignInUserType()
        val jsonStr = runBlocking {
            context.authStore.data.first()
        }[USER] ?: throw Exception("No User Found!")

        return when (userType) {
            Const.Params.UserType.STUDENT -> {
                GsonUtil.fromJson<StudentEntity>(jsonStr)
            }
            Const.Params.UserType.TEACHER -> {
                GsonUtil.fromJson<TeacherEntity>(jsonStr)
            }
            else -> throw Exception("No User Found!")
        }
    }

    fun getUserIdAndType() : Pair<Int, String> {
        val userId = try {
            when(val user = getSignInUser()) {
                is StudentEntity -> user.id
                is TeacherEntity -> user.id
                else -> throw Exception("Sign in first to complete this action")
            }
        } catch (ex: Exception) {
            throw Exception("Sign in first to complete this action")
        }

        return Pair(userId, getSignInUserType())
    }

    suspend fun storeSignInStudent(student: StudentEntity) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[USER] = GsonUtil.toJson(student)
            auth[USER_TYPE] = Const.Params.UserType.STUDENT
        }
    }

    suspend fun storeSignInTeacher(teacher: TeacherEntity) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[USER] = GsonUtil.toJson(teacher)
            auth[USER_TYPE] = Const.Params.UserType.TEACHER
        }
    }

    private suspend fun storeAuthToken(token: String) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[AUTH_TOKEN] = token
        }
        Prefs.authToken = token
    }

    suspend fun signIn(email: String, password: String, userType: String): Any {
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = when(userType) {
            Const.Params.UserType.STUDENT -> helper.signInStudent(email, password, deviceId)
            Const.Params.UserType.TEACHER -> helper.signInTeacher(email, password, deviceId)
            else -> throw Exception("Invalid User Type!")
        }
        data.first.let { user ->
            when (user) {
                is StudentEntity -> storeSignInStudent(user)
                is TeacherEntity -> storeSignInTeacher(user)
                else -> throw Exception("Invalid User Type!")
            }
        }
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String
    ): StudentEntity {
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signUpStudent(name, id, reg, facultyId, batchId, session,
            email, deviceId)
        storeSignInStudent(data.first)
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
    ): TeacherEntity {
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signUpTeacher(name, designation, department, email, password,
            facultyId, deviceId)
        storeSignInTeacher(data.first)
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signOut(): String {
        val userType = getSignInUserType()
        val id = when(val user = getSignInUser()) {
            is StudentEntity -> user.id
            is TeacherEntity -> user.id
            else -> throw Exception("Invalid account")
        }
        val data = helper.signOut(id, userType)
        deleteAll()

        return data
    }

    suspend fun changePassword(oldPassword: String, newPassword: String): String {
        val userType = getSignInUserType()
        val id = when(val user = getSignInUser()) {
            is StudentEntity -> user.id
            is TeacherEntity -> user.id
            else -> throw Exception("Invalid account")
        }
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        helper.changePassword(id, userType, oldPassword, newPassword, deviceId).also {
            it.second?.let { newAuthToken ->
                storeAuthToken(newAuthToken)
            }
            return it.first
        }
    }

    suspend fun forgotPassword(userType: String, email: String): String {
        return helper.forgotPassword(userType, email)
    }

    suspend fun deleteAll() {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth.remove(USER)
            auth.remove(USER_TYPE)
            auth.remove(AUTH_TOKEN)
        }
        Prefs.authToken = ""
    }

    suspend fun updateDataRefreshState() {
        val config = dbService.getLatest()
        config?.let {
            it.forceRefreshDone = true
            dbService.update(it)
        }
    }
}