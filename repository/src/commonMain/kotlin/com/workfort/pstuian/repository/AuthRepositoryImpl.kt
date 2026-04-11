package com.workfort.pstuian.repository

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.database.service.ConfigDbService
import com.workfort.pstuian.model.ConfigEntity
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.model.StudentEntity
import com.workfort.pstuian.model.TeacherEntity
import com.workfort.pstuian.model.dto.toDto
import com.workfort.pstuian.networking.domain.AuthApiHelper
import com.workfort.pstuian.sharedpref.Prefs
import com.workfort.pstuian.util.helper.GsonUtil
import com.workfort.pstuian.util.helper.PlatformUtil

class AuthRepositoryImpl(
    private val dbService: ConfigDbService,
    private val helper: AuthApiHelper,
    private val prefs: Prefs,
    private val gsonUtil: GsonUtil,
) : AuthRepository {

    override suspend fun getConfig(): ConfigEntity {
        val oldConfig = dbService.getLatest()
        var newConfig = helper.getConfig().toEntity()
        if(oldConfig == null || oldConfig != newConfig) {
            dbService.insert(newConfig)
        } else {
            // pass local states(forceRefreshDone and forceUpdateDone) into the new state
            if(oldConfig == newConfig) newConfig = oldConfig
        }

        return newConfig
    }

    override suspend fun getAllDevices(page: Int): List<DeviceEntity> {
        val userType = getSignInUserType()
        val id = when(val user = getSignInUser()) {
            is StudentEntity -> user.id
            is TeacherEntity -> user.id
            else -> throw Exception("Invalid account")
        }
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        return helper.getAllDevices(id, userType, deviceId, page).map {
            it.toEntity()
        }
    }

    override suspend fun registerDevice(
        fcmToken: String,
        lat: String,
        lng: String,
    ): DeviceEntity {
        val deviceId = if(prefs.deviceId.isNullOrEmpty()) {
            val newId = PlatformUtil.getLocalIpAddress() // Dummy for now, should be a real UUID
            prefs.deviceId = newId
            newId
        } else prefs.deviceId
        val newDevice = DeviceEntity(
            id = deviceId!!,
            fcmToken = fcmToken,
            model = PlatformUtil.getDeviceName(),
            androidVersion = PlatformUtil.deviceVersionName,
            appVersionCode = PlatformUtil.appVersionCode,
            appVersionName = PlatformUtil.appVersionName,
            ipAddress = PlatformUtil.getLocalIpAddress(),
            lat = lat,
            lng = lng,
            locale = PlatformUtil.getLocaleLanguage()
        )

        // device is registered and updated
        // if(newDevice == getRegisteredDevice()) {
        //    return newDevice
        // }

        // device is not registered yet/device is not updated
        helper.registerDevice(newDevice.toDto()).also { device ->
            // storeRegisteredDevice(device)
            return device.toEntity()
        }
    }

    override suspend fun updateFcmToken(
        fcmToken: String
    ): DeviceEntity {
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Device is not registered yet!")

        helper.updateFcmToken(deviceId, fcmToken).also { device ->
            prefs.fcmToken = fcmToken
            // storeRegisteredDevice(device)
            return device.toEntity()
        }
    }

    override fun getSignInUserType(): String {
        return prefs.userType ?: throw Exception("Unknown user type")
    }

    override fun getSignInUser(): Any {
        val userType = getSignInUserType()
        val jsonStr = prefs.user ?: ""

        return when (userType) {
            NetworkConst.Params.UserType.STUDENT -> {
                gsonUtil.fromJson<StudentEntity>(jsonStr)
            }
            NetworkConst.Params.UserType.TEACHER -> {
                gsonUtil.fromJson<TeacherEntity>(jsonStr)
            }
            else -> throw Exception("No User Found!")
        }
    }

    override fun getUserIdAndType() : Pair<Int, String> {
        val userId = try {
            when(val user = getSignInUser()) {
                is StudentEntity -> user.id
                is TeacherEntity -> user.id
                else -> throw Exception("Sign in first to complete this action")
            }
        } catch (_: Exception) {
            throw Exception("Sign in first to complete this action")
        }

        return Pair(userId, getSignInUserType())
    }

    override suspend fun storeSignInStudent(student: StudentEntity) {
        val jsonStr = gsonUtil.toJson(student)
        prefs.user = jsonStr
        prefs.userType = NetworkConst.Params.UserType.STUDENT
    }

    override suspend fun storeSignInTeacher(teacher: TeacherEntity) {
        val jsonStr = gsonUtil.toJson(teacher)
        prefs.user = jsonStr
        prefs.userType = NetworkConst.Params.UserType.TEACHER
    }

    override suspend fun signIn(email: String, password: String, userType: String): Any {
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = when(userType) {
            NetworkConst.Params.UserType.STUDENT -> helper.signInStudent(email, password, deviceId)
            NetworkConst.Params.UserType.TEACHER -> helper.signInTeacher(email, password, deviceId)
            else -> throw Exception("Invalid User Type!")
        }
        val user = data.first
        when (user) {
            is StudentEntity -> storeSignInStudent(user)
            is TeacherEntity -> storeSignInTeacher(user)
            else -> throw Exception("Invalid User Type!")
        }
        prefs.authToken = data.second

        return user
    }

    override suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        password: String,
    ): StudentEntity {
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signUpStudent(
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
        return data.first.toEntity()
    }

    override suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
    ): TeacherEntity {
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signUpTeacher(name, designation, department, email, password,
            facultyId, deviceId)
        return data.first.toEntity()
    }

    override suspend fun signOut(fromAllDevice: Boolean): String {
        val userType = getSignInUserType()
        val id = when(val user = getSignInUser()) {
            is StudentEntity -> user.id
            is TeacherEntity -> user.id
            else -> throw Exception("Invalid account")
        }
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signOut(id, userType, deviceId, fromAllDevice)
        deleteAll()

        return data
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): String {
        val userType = getSignInUserType()
        val id = when(val user = getSignInUser()) {
            is StudentEntity -> user.id
            is TeacherEntity -> user.id
            else -> throw Exception("Invalid account")
        }
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        helper.changePassword(id, userType, oldPassword, newPassword, deviceId).also {
            it.second?.let { newAuthToken ->
                prefs.authToken = newAuthToken
            }
            return it.first
        }
    }

    override suspend fun forgotPassword(userType: String, email: String): String {
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        return helper.forgotPassword(userType, email, deviceId)
    }

    override suspend fun emailVerification(userType: String, email: String): String {
        val deviceId = prefs.deviceId
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        return helper.emailVerification(userType, email, deviceId)
    }

    override suspend fun deleteAll() {
        prefs.authToken = ""
        prefs.user = ""
        prefs.userType = ""
    }

    override suspend fun updateDataRefreshState() {
        val config = dbService.getLatest()
        config?.let {
            it.forceRefreshDone = true
            dbService.update(it)
        }
    }

    override suspend fun deleteAccount(password: String): String {
        val userType = getSignInUserType()
        val (userId, email) = when(val user = getSignInUser()) {
            is StudentEntity -> user.id to user.email
            is TeacherEntity -> user.id to user.email
            else -> throw Exception("Invalid account")
        }
        if(email.isNullOrEmpty()) throw Exception("Invalid account!")
        val data = helper.deleteAccount(userId, userType, email, password)
        deleteAll()

        return data
    }
}
