package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.firebase.FirebaseAuthDataSource
import com.workfort.pstuian.data.remote.firebase.FirebaseUserPresenceDataSource
import com.workfort.pstuian.featuredomain.model.AuthUser
import com.workfort.pstuian.featuredomain.model.DomainError
import com.workfort.pstuian.featuredomain.model.DomainErrorCode
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.SharedPrefKey
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.featuredomain.model.getOrElse
import com.workfort.pstuian.featuredomain.model.map
import com.workfort.pstuian.featuredomain.model.onFailure
import com.workfort.pstuian.featuredomain.model.onSuccess
import com.workfort.pstuian.featuredomain.repository.AuthRepository
import com.workfort.pstuian.featuredomain.repository.SharedPrefRepository
import com.workfort.pstuian.featuredomain.repository.UserPresenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl(
    private val helper: AuthApiHelper,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val userPresenceRepository: UserPresenceRepository,
    private val sharedPrefRepository: SharedPrefRepository,
    private val domainErrorMapper: DomainErrorMapper,
) : AuthRepository {

    private fun getDeviceId(): String = sharedPrefRepository.getString(SharedPrefKey.DEVICE_ID) ?: ""
    private val invalidDevice = DomainError(DomainErrorCode.Auth.DeviceNotFound)
    private val invalidAuthUser = DomainError(DomainErrorCode.Auth.UserAuthNotFound)

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

    override suspend fun observeSignedInAuthUser(): Flow<AuthUser?> {
        return firebaseAuthDataSource.observeCurrentUser().map { result ->
            result?.let { (id, dto) -> dto.toAuthUser(id) }
        }
    }

    override suspend fun signIn(userType: UserType, email: String, password: String): DomainResult<User> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }

        // sign in
        val signInResult = when(userType) {
            UserType.STUDENT -> {
                helper.signInStudent(email, password, deviceId)
                    .toDomainResult(domainErrorMapper)
                    .map { (dto, authToken) ->
                        sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, authToken)
                        dto.toModel()
                    }
            }
            UserType.TEACHER -> {
                helper.signInTeacher(email, password, deviceId)
                    .toDomainResult(domainErrorMapper)
                    .map { (dto, authToken) ->
                        sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, authToken)
                        dto.toModel()
                    }
            }
            else -> {
                DomainResult.failure(
                    DomainError(DomainErrorCode.Auth.InvalidParam, Exception("Invalid User Type!"))
                )
            }
        }

        if (signInResult.isSuccess) {
            // validate auth user
            firebaseAuthDataSource.signIn(email, password)
                .toDomainResult(domainErrorMapper)
                .map { (id, dto) -> dto.toAuthUser(id) }
                .getOrElse { error ->
                    return DomainResult.failure(error)
                }
        }

        return signInResult
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
    ): DomainResult<Unit> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }

        // sign up
        helper.signUpStudent(
            name,
            id,
            reg,
            facultyId,
            batchId,
            session,
            email,
            password,
            deviceId,
        ).toDomainResult(domainErrorMapper).onFailure {
            return DomainResult.failure(it)
        }

        // register auth user
        return completeFirebaseSignUp(UserType.STUDENT.type, email, password).map { }
    }

    override suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        password: String,
    ): DomainResult<Unit> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }

        // sign up
        helper.signUpTeacher(
            name,
            facultyId,
            designation,
            department,
            email,
            password,
            deviceId,
        ).toDomainResult(domainErrorMapper).onFailure {
            return DomainResult.failure(it)
        }

        // register auth user
        return completeFirebaseSignUp(UserType.TEACHER.type, email, password).map { }
    }

    override suspend fun creatLegacyUserAuth(
        userType: UserType,
        email: String,
        password: String
    ): DomainResult<Unit> {
        return completeFirebaseSignUp(userType.type, email, password).map { }
    }

    override suspend fun signOut(userType: UserType, fromAllDevice: Boolean): DomainResult<Unit> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }
        val userId = getAuthUser()?.userId ?: return DomainResult.failure(invalidAuthUser)

        return helper.signOut(userId, userType.type, deviceId, fromAllDevice)
            .toDomainResult(domainErrorMapper)
            .onSuccess {
                userPresenceRepository.removeUserPresence(userId)
                firebaseAuthDataSource.signOut()
                removeAuthPrefs()
            }
    }

    override suspend fun changePassword(
        userType: UserType,
        oldPassword: String,
        newPassword: String,
    ): DomainResult<Unit> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }
        val authUser = getAuthUser() ?: return DomainResult.failure(invalidAuthUser)

        return helper.changePassword(
            userType.type,
            authUser.email,
            oldPassword,
            newPassword,
            deviceId
        )
            .toDomainResult(domainErrorMapper)
            .onSuccess { authToken ->
                sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, authToken)
                DomainResult.success(Unit)
            }
            .map { } // No need to return any data
    }

    override suspend fun resetPassword(email: String): DomainResult<Unit> {
        return firebaseAuthDataSource.resetPassword(email).toDomainResult(domainErrorMapper)
    }

    override suspend fun sendVerificationEmail(email: String, password: String): DomainResult<Unit> {
        return firebaseAuthDataSource.sendVerificationEmail(email, password).toDomainResult(domainErrorMapper)
    }

    override suspend fun deleteAccount(userType: UserType, password: String): DomainResult<Unit> {
        val authUser = getAuthUser() ?: return DomainResult.failure(invalidAuthUser)

        firebaseAuthDataSource.signOut()
        return helper.deleteAccount(authUser.email, userType.type, password)
            .toDomainResult(domainErrorMapper)
            .onSuccess { removeAuthPrefs() }
    }

    override suspend fun removeAuthPrefs() {
        sharedPrefRepository.remove(SharedPrefKey.AUTH_TOKEN)
    }

    private suspend fun completeFirebaseSignUp(
        userType: String,
        email: String,
        password: String,
    ): DomainResult<AuthUser> {
        val result = firebaseAuthDataSource.signUp(email, password)
            .toDomainResult(domainErrorMapper)
            .map { (id, dto) -> dto.toAuthUser(id) }

        if (result.isSuccess) {
            result.getOrNull()?.let { authUser ->
                helper.updateUserId(authUser.userId, userType, email, password) // update auth user id
                firebaseAuthDataSource.signOut() // user should sign in after email verification
            }
        }

        return result
    }
}
