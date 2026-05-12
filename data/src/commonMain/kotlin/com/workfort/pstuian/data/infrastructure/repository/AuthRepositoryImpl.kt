package com.workfort.pstuian.data.infrastructure.repository

import com.workfort.pstuian.data.mapper.DomainErrorMapper
import com.workfort.pstuian.data.mapper.toDomainResult
import com.workfort.pstuian.data.remote.domain.AuthApiHelper
import com.workfort.pstuian.data.remote.firebase.FirebaseAuthDataSource
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
        return firebaseAuthDataSource.getCurrentUser()?.toAuthUser()
    }

    override fun isUserSignedIn(): Boolean {
        return firebaseAuthDataSource.isUserLoggedIn()
    }

    override fun isUserEmailVerified(): Boolean {
        return firebaseAuthDataSource.isUserEmailVerified()
    }

    override suspend fun syncAuthTokenToPreferences(forceRefresh: Boolean) {
        val token = firebaseAuthDataSource.getAuthToken(forceRefresh)
        sharedPrefRepository.putString(SharedPrefKey.AUTH_TOKEN, token)
    }

    override suspend fun observeSignedInAuthUser(): Flow<AuthUser?> {
        return firebaseAuthDataSource.observeCurrentUser().map { it?.toAuthUser() }
    }

    override suspend fun signIn(userType: UserType, email: String, password: String): DomainResult<User> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }

        firebaseAuthDataSource.signIn(email, password).toDomainResult(domainErrorMapper).onFailure {
            return DomainResult.failure(it)
        }

        // update auth-token for api->header
        syncAuthTokenToPreferences()

        // validate sign in
        return when(userType) {
            UserType.STUDENT -> {
                helper.validateStudentSignIn(deviceId).toDomainResult(domainErrorMapper).map { it.toModel() }
            }
            UserType.TEACHER -> {
                helper.validateTeacherSignIn(deviceId).toDomainResult(domainErrorMapper).map { it.toModel() }
            }
            else -> {
                DomainResult.failure(
                    DomainError(DomainErrorCode.Auth.InvalidParam, Exception("Invalid User Type!")),
                )
            }
        }.onFailure {
            firebaseAuthDataSource.signOut()
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
    ): DomainResult<Unit> {
        // validate device
        val deviceId = getDeviceId().ifBlank { return DomainResult.failure(invalidDevice) }

        // Auth SignUp
        firebaseAuthDataSource.signUp(email, password).toDomainResult(domainErrorMapper).getOrElse {
            return DomainResult.failure(it)
        }

        // update auth-token for api->header
        syncAuthTokenToPreferences()

        // sign up
        val result = helper.signUpStudent(
            name,
            id,
            reg,
            facultyId,
            batchId,
            session,
            deviceId,
        ).toDomainResult(domainErrorMapper)

        if (result.isSuccess) {
            firebaseAuthDataSource.sendVerificationEmail(email, password)
            firebaseAuthDataSource.signOut() // email verification is needed before signing in
        } else {
            firebaseAuthDataSource.deleteAccount(email, password) // delete auth account to avoid conflict on retry
        }

        return result
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

        // Auth SignUp
        firebaseAuthDataSource.signUp(email, password).toDomainResult(domainErrorMapper).getOrElse {
            return DomainResult.failure(it)
        }

        // sign up
        val result = helper.signUpTeacher(
            name,
            facultyId,
            designation,
            department,
            deviceId,
        ).toDomainResult(domainErrorMapper)

        if (result.isSuccess) {
            firebaseAuthDataSource.sendVerificationEmail(email, password)
            firebaseAuthDataSource.signOut() // email verification is needed before signing in
        } else {
            firebaseAuthDataSource.deleteAccount(email, password) // delete auth account to avoid conflict on retry
        }

        return result
    }

    override suspend fun signOut(userType: UserType, clearAllSession: Boolean): DomainResult<Unit> {
        val userId = getAuthUser()?.userId ?: return DomainResult.failure(invalidAuthUser)
        userPresenceRepository.removeUserPresence(userId) // clear user presence

        helper.signOut(userType.type, clearAllSession).toDomainResult(domainErrorMapper).getOrElse {
            return DomainResult.failure(it)
        }

        return firebaseAuthDataSource.signOut()
            .toDomainResult(domainErrorMapper)
            .onSuccess { removeAuthPrefs() }
    }

    override suspend fun changePassword(
        email: String,
        userType: UserType,
        oldPassword: String,
        newPassword: String,
    ): DomainResult<Unit> {
        return firebaseAuthDataSource.updatePassword(oldPassword, newPassword)
            .toDomainResult(domainErrorMapper)
            .onSuccess { syncAuthTokenToPreferences(forceRefresh = true) }
    }

    override suspend fun sendResetPasswordLink(email: String): DomainResult<Unit> {
        return firebaseAuthDataSource.sendPasswordResetEmail(email).toDomainResult(domainErrorMapper)
    }

    override suspend fun resetPasswordReset(oobCode: String, newPassword: String): DomainResult<Unit> {
        return firebaseAuthDataSource.confirmPasswordReset(oobCode, newPassword).toDomainResult(domainErrorMapper)
    }

    override suspend fun sendVerificationEmail(email: String, password: String): DomainResult<Unit> {
        return firebaseAuthDataSource.sendVerificationEmail(email, password).toDomainResult(domainErrorMapper)
    }

    override suspend fun activateAccount(
        userType: UserType,
        email: String,
        password: String
    ): DomainResult<Unit> {
        // authenticate the user first
        firebaseAuthDataSource.signIn(email, password).toDomainResult(domainErrorMapper).onFailure {
            return DomainResult.failure(it)
        }

        return helper.activateAccount(userType.type)
            .toDomainResult(domainErrorMapper)
            .onSuccess { signOut(userType) } // should sign in again after activating
    }

    override suspend fun deactivateAccount(
        userType: UserType,
        email: String,
        password: String,
    ): DomainResult<Unit> {
        return helper.deactivateAccount(userType.type)
            .toDomainResult(domainErrorMapper)
            .onSuccess { signOut(userType) }
    }

    override suspend fun removeAuthPrefs() {
        sharedPrefRepository.remove(SharedPrefKey.AUTH_TOKEN)
    }
}
