package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.NetworkConst
import com.workfort.pstuian.data.dto.toDto
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.featuredomain.model.ConfigEntity
import com.workfort.pstuian.featuredomain.model.DeviceEntity
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.StudentEntity
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.util.PlatformInfo
import com.workfort.pstuian.util.helper.JsonParser

class AuthRepositoryImpl(
    private val helper: AuthApiHelper,
    private val sharedPrefRepository: SharedPrefRepository,
    private val jsonParser: JsonParser,
    private val platformInfo: PlatformInfo,
) : AuthRepository {
    private var config: ConfigEntity? = null

    override suspend fun getConfig(): ConfigEntity {
        var newConfig = helper.getConfig().toEntity()
        if (config == null || config != newConfig) {
            config = newConfig
        } else {
            // pass local states(forceRefreshDone and forceUpdateDone) into the new state
            val oldConfig = config!!
            newConfig.forceRefreshDone = oldConfig.forceRefreshDone
            newConfig.forceUpdateDone = oldConfig.forceUpdateDone
            config = newConfig
        }

        return config!!
    }

    override suspend fun getAllDevices(page: Int): List<DeviceEntity> {
        val userType = getSignInUserType()
        val id = when(val user = getSignInUser()) {
            is StudentEntity -> user.id
            is TeacherEntity -> user.id
            else -> throw Exception("Invalid account")
        }
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
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
        val existingDeviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        val deviceId = if(existingDeviceId.isNullOrEmpty()) {
            val newId = platformInfo.deviceId
            sharedPrefRepository.putString(SharedPrefKey.DEVICE_ID, newId)
            newId
        } else existingDeviceId
        val newDevice = DeviceEntity(
            id = deviceId,
            fcmToken = fcmToken,
            model = platformInfo.model,
            androidVersion = "",
            appVersionCode = 0,
            appVersionName = platformInfo.appVersion,
            ipAddress = platformInfo.getLocalIpAddress(),
            lat = lat,
            lng = lng,
            locale = platformInfo.locale,
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
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Device is not registered yet!")

        helper.updateFcmToken(deviceId, fcmToken).also { device ->
            sharedPrefRepository.putString(SharedPrefKey.FCM_TOKEN, fcmToken)
            // storeRegisteredDevice(device)
            return device.toEntity()
        }
    }

    override fun getSignInUserType(): String {
        return sharedPrefRepository.getString(SharedPrefKey.USER_TYPE)
            ?: throw Exception("Unknown user type")
    }

    override fun getSignInUser(): Any {
        val userType = getSignInUserType()
        val jsonStr = sharedPrefRepository.getString(SharedPrefKey.USER) ?: ""

        return when (userType) {
            NetworkConst.Params.UserType.STUDENT -> {
                jsonParser.fromJson<StudentEntity>(jsonStr)
            }
            NetworkConst.Params.UserType.TEACHER -> {
                jsonParser.fromJson<TeacherEntity>(jsonStr)
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
        val jsonStr = jsonParser.toJson(student)
        sharedPrefRepository.apply {
            putString(SharedPrefKey.USER, jsonStr)
            putString(SharedPrefKey.USER_TYPE, NetworkConst.Params.UserType.STUDENT)
        }
    }

    override suspend fun storeSignInTeacher(teacher: TeacherEntity) {
        val jsonStr = jsonParser.toJson(teacher)
        sharedPrefRepository.apply {
            putString(SharedPrefKey.USER, jsonStr)
            putString(SharedPrefKey.USER_TYPE, NetworkConst.Params.UserType.TEACHER)
        }
    }

    override suspend fun signIn(email: String, password: String, userType: String): Any {
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val (user, authToken) = when(userType) {
            NetworkConst.Params.UserType.STUDENT -> helper.signInStudent(email, password, deviceId)
            NetworkConst.Params.UserType.TEACHER -> helper.signInTeacher(email, password, deviceId)
            else -> throw Exception("Invalid User Type!")
        }
        when (user) {
            is StudentEntity -> storeSignInStudent(user)
            is TeacherEntity -> storeSignInTeacher(user)
            else -> throw Exception("Invalid User Type!")
        }
        sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, authToken)

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
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
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
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
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
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
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
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        helper.changePassword(id, userType, oldPassword, newPassword, deviceId)
            .also { (user, authToken) ->
                sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, authToken)
                return user
            }
    }

    override suspend fun forgotPassword(userType: String, email: String): String {
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        return helper.forgotPassword(userType, email, deviceId)
    }

    override suspend fun emailVerification(userType: String, email: String): String {
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        return helper.emailVerification(userType, email, deviceId)
    }

    override suspend fun deleteAll() {
        sharedPrefRepository.remove(SharedPrefKey.AUTH_TOKEN)
        sharedPrefRepository.remove(SharedPrefKey.USER)
        sharedPrefRepository.remove(SharedPrefKey.USER_TYPE)
    }

    override suspend fun updateDataRefreshState() {
        config?.let {
            it.forceRefreshDone = true
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
