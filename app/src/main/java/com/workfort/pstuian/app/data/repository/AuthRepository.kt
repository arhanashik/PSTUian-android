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
import com.workfort.pstuian.app.data.local.device.DeviceEntity
import com.workfort.pstuian.app.data.local.pref.Prefs
import com.workfort.pstuian.app.data.local.student.StudentEntity
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
        val unregisteredDevice = DeviceEntity(
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

        if(unregisteredDevice == getRegisteredDevice()) {
            return unregisteredDevice
        }

        helper.registerDevice(unregisteredDevice).also { device ->
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

    fun getSignInUser(): StudentEntity {
        val context = PstuianApp.getBaseApplicationContext()
        val jsonStr = runBlocking {
            context.authStore.data.first()
        }[USER]?: throw Exception("No User Found!")

        return GsonUtil.fromJson(jsonStr)
    }

    suspend fun storeSignInUser(student: StudentEntity) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[USER] = GsonUtil.toJson(student)
        }
    }

    private suspend fun storeAuthToken(token: String) {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth[AUTH_TOKEN] = token
        }
        Prefs.authToken = token
    }

    suspend fun signIn(email: String, password: String, userType: String): StudentEntity {
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signIn(email, password, userType, deviceId)
        storeSignInUser(data.first)
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signUp(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String
    ): StudentEntity {
        val deviceId = Prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signUp(name, id, reg, facultyId, batchId, session, deviceId)
        storeSignInUser(data.first)
        storeAuthToken(data.second)

        return data.first
    }

    suspend fun signOut(id: Int, userType: String): String {
        val data = helper.signOut(id, userType)
        deleteAll()

        return data
    }

    suspend fun deleteAll() {
        val context = PstuianApp.getBaseApplicationContext()
        context.authStore.edit { auth ->
            auth.remove(USER)
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