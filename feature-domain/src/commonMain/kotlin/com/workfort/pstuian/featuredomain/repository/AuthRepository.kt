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

    /**
     * Firebase auth sign up is started from this KMM project.
     * So the users who signed up before, needs to be signed up with Firebase Auth.
     * But this should be done after confirming signIn in server.
     */
    suspend fun creatLegacyUserAuth(userType: UserType, email: String, password: String): DomainResult<Unit>

    suspend fun signOut(userType: UserType, fromAllDevice: Boolean = false): DomainResult<Unit>

    suspend fun changePassword(
        email: String,
        userType: UserType,
        oldPassword: String,
        newPassword: String,
    ): DomainResult<Unit>

    suspend fun sendResetPasswordLink(email: String): DomainResult<Unit>

    suspend fun resetPasswordReset(oobCode: String, newPassword: String): DomainResult<Unit>
    suspend fun sendVerificationEmail(email: String, password: String): DomainResult<Unit>

    suspend fun deleteAccount(userType: UserType, password: String): DomainResult<Unit>
    suspend fun removeAuthPrefs()
}