package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.firebase.FirebaseAuthDataSource
import com.workfort.pstuian.featuredomain.model.AuthUser
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.TeacherEntity
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.util.helper.JsonParser

class AuthRepositoryImpl(
    private val helper: AuthApiHelper,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val sharedPrefRepository: SharedPrefRepository,
    private val domainErrorMapper: DomainErrorMapper,
    private val jsonParser: JsonParser,
) : AuthRepository {

    override fun getAuthUser(): AuthUser? {
        return firebaseAuthDataSource.getCurrentUser()?.let { (userId, dto) ->
            dto.toAuthUser(userId)
        }
    }

    override fun isUserSignedIn(): Boolean {
        return firebaseAuthDataSource.isUserLoggedIn()
    }

    override fun isUserEmailVerified(): Boolean {
        return firebaseAuthDataSource.isUserEmailVerified()
    }

    override fun getSignInUserType(): UserType? {
        val userTypeStr = sharedPrefRepository.getString(SharedPrefKey.USER_TYPE) ?: return null
        return UserType.create(userTypeStr)
    }

    override suspend fun storeSignInTeacher(teacher: TeacherEntity) {
        val jsonStr = jsonParser.toJson(teacher)
        sharedPrefRepository.apply {
            putString(SharedPrefKey.USER, jsonStr)
            putString(SharedPrefKey.USER_TYPE, NetworkConst.Params.UserType.TEACHER)
        }
    }

    override suspend fun signIn(email: String, password: String, userType: UserType): User {
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")

        val (user, authToken) = when(userType) {
            UserType.STUDENT -> helper.signInStudent(email, password, deviceId)
            UserType.TEACHER -> helper.signInTeacher(email, password, deviceId)
            else -> throw Exception("Invalid User Type!")
        }

        sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, authToken)
        sharedPrefRepository.putString(SharedPrefKey.USER_TYPE, userType.type)

        return when (user) {
            is StudentDto -> user.toModel()
            is TeacherDto -> user.toModel()
            else -> throw Exception("Invalid User Type!")
        }
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
    ): User.Student {
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
        return data.first.toModel()
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
        val userType = getSignInUserType()?.type ?: throw Exception("Invalid user!")
        val userId = getAuthUser()?.userId ?: throw Exception("Invalid user!")
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        val data = helper.signOut(userId, userType, deviceId, fromAllDevice)
        deleteAll()

        return data
    }

    override suspend fun changePassword(oldPassword: String, newPassword: String): String {
        val userType = getSignInUserType()?.type ?: throw Exception("Invalid user!")
        val userId = getAuthUser()?.userId ?: throw Exception("Invalid user!")
        val deviceId = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID)
        if(deviceId.isNullOrEmpty()) throw Exception("Invalid device!")
        helper.changePassword(userId, userType, oldPassword, newPassword, deviceId)
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
        sharedPrefRepository.remove(SharedPrefKey.APP_USAGE_ROLE)
    }

    override suspend fun deleteAccount(password: String): String {
        val userType = getSignInUserType()?.type ?: throw Exception("Invalid user!")
        val user = getAuthUser() ?: throw Exception("Invalid user!")
        val data = helper.deleteAccount(user.userId, userType, user.email, password)
        deleteAll()

        return data
    }
}
