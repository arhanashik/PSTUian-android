package com.workfort.pstuian.featuredomain.repository

import com.workfort.pstuian.featuredomain.model.AuthUser
import com.workfort.pstuian.featuredomain.model.DomainResult
import com.workfort.pstuian.featuredomain.model.User
import com.workfort.pstuian.featuredomain.model.UserType
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun getAuthUser(): AuthUser?

    fun isUserSignedIn(): Boolean

    fun isUserEmailVerified(): Boolean

    /**
     * Persists the current provider auth token (e.g. Firebase ID token) for API clients.
     * When [forceRefresh] is true, the provider returns a fresh token when applicable.
     */
    suspend fun syncAuthTokenToPreferences(forceRefresh: Boolean = false)

    suspend fun observeSignedInAuthUser(): Flow<AuthUser?>

    suspend fun signIn(userType: UserType, email: String, password: String): DomainResult<User>

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        password: String,
    ): DomainResult<Unit>

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        password: String,
    ): DomainResult<Unit>

    suspend fun signOut(userType: UserType, clearAllSession: Boolean = false): DomainResult<Unit>

    suspend fun changePassword(
        email: String,
        userType: UserType,
        oldPassword: String,
        newPassword: String,
    ): DomainResult<Unit>

    suspend fun sendResetPasswordLink(email: String): DomainResult<Unit>

    suspend fun resetPasswordReset(oobCode: String, newPassword: String): DomainResult<Unit>

    suspend fun sendVerificationEmail(email: String, password: String): DomainResult<Unit>

    suspend fun activateAccount(
        userType: UserType,
        email: String,
        password: String,
    ): DomainResult<Unit>

    suspend fun deactivateAccount(
        userType: UserType,
        email: String,
        password: String,
    ): DomainResult<Unit>

    suspend fun removeAuthPrefs()
}